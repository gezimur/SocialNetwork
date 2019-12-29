package ru.gena.itmo.SocialNetwork.SocialNetwork;

import ru.gena.itmo.SocialNetwork.SocialNetwork.content.PatternsTree;
import ru.gena.itmo.SocialNetwork.SocialNetwork.content.TreesElement;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

public class Designer {

    public static String createSVGtoPatternsTree(PatternsTree tree){
        StringBuilder svg = new StringBuilder("<svg xmlns=\"http://www.w3.org/2000/svg\" version=\"1.1\"\n" +
                "viewBox=\"0 0 500 500\" preserveAspectRatio=\"xMaxYMax none\">\n");

        //реализовать проход в глубину
        //массив для проверки посещения и очередь посещения
        boolean[] used = new boolean[tree.getNumberOfElements()];
        ArrayDeque<ArrayList<TreesElement>> queue = new ArrayDeque<>();
        //добавляем первый элемент в очередь
        queue.add(tree.getLine(0));
        ArrayList<TreesElement> v;
        used[0] = true;

        int n = 0;
        int row = 1;
        int ySpace = 20;
        while (!queue.isEmpty()){//пока очередь посещения не пуста
            v = queue.pop();//берем следующую в очереди вершину
            for (int i = 1; i < v.size(); i++){//идем по всем связанным элементам
                int w = v.get(i).id;
                int elementsInRow = 5;//(v.size() > 7)? 6 : v.size() - 1;
                String name = v.get(i).name;
                int x2, y2;
                if (!used[w]) {//проверяем посещали ли мы паттерн раньше

                    x2 = 100 / (2 * elementsInRow) + 100 / elementsInRow * n;//определяем новые координаты
                    y2 = 40 * row;

                    tree.getLine(w).get(0).newCoordinate(x2,y2);//сохраняем координаты
                    queue.add(tree.getLine(w));//добовляем в очередь элемент

                }else{//иначе берем его координаты
                    x2 = tree.getLine(w).get(0).x;
                    y2 = tree.getLine(w).get(0).y;
                }
                //рисуем линию
                svg.append("<line x1=\"");
                svg.append(v.get(0).x);
                svg.append("%\" y1=\"");
                svg.append(v.get(0).y + ySpace);
                svg.append("\" x2=\"");
                svg.append(x2);
                svg.append("%\" y2=\"");
                svg.append(y2);
                svg.append("\" stroke=\"black\"></line>\n");
                if (!used[w]) {//проверяем посещали ли мы паттерн раньше
                    //пишем его название
                    svg.append("<text id=\"");
                    svg.append(w);
                    svg.append("\" x=\"");
                    svg.append(x2 - name.length() / 2);
                    svg.append("%\" y=\"");
                    svg.append(y2 + ySpace / 2);
                    svg.append("\"><a href=\"/pattern/id");
                    svg.append(toNeddedForm("" + w));
                    svg.append("\">");
                    svg.append(name);
                    svg.append("</a></text>\n");
                }
                used[w] = true;//отмечаем как посещенного
                row = row + (n + 1) / elementsInRow;
                n = (n + 1) % elementsInRow;
            }
            row += 1;
            n = 0;
        }

        svg.append("</svg>\n");
        return svg.toString();
    }

    public static String toNeddedForm(String i){
        int id = Integer.parseInt(i);
        int useFullId = Integer.parseInt(i);
        int numberOfNumerals = 0;
        while (id / 10 != 0){
            id /= 10;
            numberOfNumerals++;
        }
        return "00000".substring(numberOfNumerals) + useFullId;
    }

    public static String textAnalysis(String text){
        int siteswapStart = text.indexOf('&');
        int siteswapEnd = text.indexOf('&', siteswapStart + 1);
        if (siteswapStart != -1 && siteswapEnd != -1 && siteswapEnd - siteswapStart > 1){
            String str = text.substring(siteswapStart + 1, siteswapEnd);
            ArrayList<Integer> siteswap = analysisSiteswap(str);
            if (siteswap != null){
                return visualize(siteswap);
            }
        }
        return text;
    }

    private static String visualize(ArrayList<Integer> siteswap){
        StringBuilder animation = new StringBuilder("<style>\n");
        int maxThrow = siteswap.get(0);
        int numberOfBalls = siteswap.get(1);
        ArrayList<Integer> neededTime = new ArrayList<>();
        int ballNumber = 0;
        int pos = 2;
        while (ballNumber < numberOfBalls) {
            neededTime.add(siteswap.get(pos));
            pos = addBallAnimationAndReturnNewPos(animation,
                    ballNumber,
                    siteswap,
                    pos,
                    maxThrow);
            ballNumber++;
        }
        animation.append("</style>\n");
        animation.append("<svg xmlns=\"http://www.w3.org/2000/svg\" version=\"1.1\"\n" +
                "viewBox=\"0 0 500 500\" preserveAspectRatio=\"xMaxYMax none\">\n");
        ballNumber = 0;
        while (ballNumber < numberOfBalls) {
            animation.append("<circle cx=\"50%\" cy=\"90\" r=\"10\" fill=\"red\" style=\"\n animation: ball");
            animation.append(ballNumber);
            animation.append(" ");
            animation.append(neededTime.get(ballNumber));
            animation.append("s ");
            animation.append(ballNumber / 2.0);
            animation.append("s infinite;\"></circle>\n");
            ballNumber++;
        }//*/
        animation.append("</svg>\n");//*/
        return animation.toString();
    }

    private static int addBallAnimationAndReturnNewPos(StringBuilder anim,
                               int ballNumber,
                               List<Integer> siteswap,
                               int pos,
                               int maxThrow){
        anim.append("\n@keyframes ball"); anim.append(ballNumber);
        anim.append(" {\n");
        int x = (ballNumber % 2 == 0)? 100 : 400;
        int newPos = addKeyframesForOneCircle(anim,
                0,
                x,
                siteswap,
                pos,
                maxThrow);
        if (siteswap.get(pos) % 2 != 0){
            x = 500 - x;
            addKeyframesForOneCircle(anim,
                    50,
                    x,
                    siteswap,
                    pos,
                    maxThrow);
            x = 500 - x;
        }
        anim.append("100% {");
        anim.append("\ncx:"); anim.append(x);
        anim.append(";\ncy:"); anim.append(450);
        anim.append(";\n}\n}\n");
        return newPos;
    }

    private static int addKeyframesForOneCircle(StringBuilder anim,
                                          int keyframe,
                                          int x,
                                          List<Integer> siteswap,
                                          int pos,
                                          int maxThrow){
        double step = (siteswap.get(pos) % 2 == 0)? 100.0 / (2 * siteswap.get(pos)) : 100.0 / (4 * siteswap.get(pos));
        int neededTime = siteswap.get(pos);
        int time = 0;
        pos++;
        while (time < neededTime){//еще нужен последний кадр и повтор если не возвращается в исходную руку
            addKeyframesToOneThrow(anim, keyframe, step, x, siteswap.get(pos), maxThrow);
            x = (siteswap.get(pos) % 2 == 0)? x : 500 - x;
            keyframe += (int)(2* step * siteswap.get(pos));
            time += siteswap.get(pos);
            pos++;
        }
        return pos;
    }

    private static void addKeyframesToOneThrow(StringBuilder anim,
                             int keyframe,
                             double step,
                             int x,
                             int throwType,
                             int maxThrow){
        int y = 450;
        anim.append(keyframe);
        anim.append("% {");
        anim.append("\ncx:"); anim.append(x);
        anim.append(";\ncy:"); anim.append(y);
        anim.append(";\nanimation-timing-function: ease-out;\n}\n");
        y = 450 - 400 / maxThrow * throwType;
        keyframe += (int)(step * throwType);
        anim.append(keyframe);
        anim.append("% {");
        anim.append("\ncy:"); anim.append(y);
        anim.append(";\nanimation-timing-function: ease-in;\n}\n");
    }

//возвращает (макс. бросок; кол-во мячей; врямя цикла для мяча1; бр. мяча; ...; кол-во мячей)
    private static ArrayList<Integer> analysisSiteswap(String siteswap){
        int siteswapLength = siteswap.length();
        if (siteswapLength > 25) { //проверка длины
            return null;
        }
        int[] siteswapN = stringToNumerals(siteswap);
        if (checkSiteswap(siteswapN, siteswapLength)){
            ArrayList<Integer> ans = new ArrayList<>();
            ans.add(getMaxThrow(siteswapN, siteswapLength));
            int numberOfBalls = getNumberOfBalls(siteswapN, siteswapLength);
            ans.add(numberOfBalls);
            for (int i = 0; i < numberOfBalls; i++){
                int ball = i % siteswapLength;
                ans.addAll(doSiteswapForBall(siteswapN, siteswapLength, ball) );
            }
            return ans;
        }
        return null;
    }

    private static ArrayList<Integer> doSiteswapForBall(int[] siteswap, int siteswapLength, int i){
        ArrayList<Integer> ans = new ArrayList<>();
        ans.add(0);//время требуемое для одного чикла
        ans.add(siteswap[i]);
        int neededTime = siteswap[i];
        int position = (i + siteswap[i]) % siteswapLength;
        while(position != i){
            ans.add(siteswap[position]);
            neededTime += siteswap[position];
            position = (position + siteswap[position]) % siteswapLength;
        }
        ans.set(0, neededTime);
        return ans;
    }

    private static int getNumberOfBalls(int[] siteswap, int siteswapLength){
        int[] coordLine = new int[siteswapLength];
        int minCoord = siteswap[0];
        for(int i = 0; i < siteswapLength; i++){
            int value = siteswap[i];
            coordLine[(i + value) % siteswapLength] = i + value;
            minCoord = (minCoord < i + value)? minCoord : i + value;
        }
        if (minCoord > siteswapLength * 2 - 1) return minCoord - (siteswapLength - 1);

        int nomberOfBalls = siteswapLength;
        minCoord = (minCoord < siteswapLength)? minCoord : siteswapLength;
        for(int i = 0; i < siteswapLength; i++){
            if (coordLine[i] >= minCoord + siteswapLength){
                nomberOfBalls ++;
            }
            if (coordLine[i] < siteswapLength){
                nomberOfBalls--;
            }
        }
        return nomberOfBalls;
    }

    private static Integer getMaxThrow(int[] siteswap, int siteswapLength){
        int maxThrow = 0;
        for (int i = 0; i < siteswapLength; i++){
            maxThrow = (maxThrow < siteswap[i])? siteswap[i] : maxThrow;
        }
        return maxThrow;
    }

    private static boolean checkSiteswap(int[] siteswap, int siteswapLength){
        boolean[] checkLine = new boolean[siteswapLength];
        for(int i = 0; i < siteswapLength; i++){
            int value = siteswap[i];
            int coordInLine = (i + value) % siteswapLength;
            if (checkLine[coordInLine]){
                return false;
            }else{
                checkLine[coordInLine] = true;
            }
        }
        return true;
    }

    private static int[] stringToNumerals(String siteswap){
        int l = siteswap.length();
        int[] ans = new int[l];
        for(int i = 0; i < l; i++){
            char c = siteswap.charAt(i);
            int value = charToNumber(c);
            ans[i] = value;
        }
        return ans;
    }

    private static int charToNumber(char c){
        if (c >= 'a' && c <= 'z'){
            return (int)c - (int)'a' + 1;
        }else if (c >= '0' && c <= '9'){
            return (int)c - (int)'0';
        }else{
            return -1;
        }
    }
}
