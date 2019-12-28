package ru.gena.itmo.SocialNetwork.SocialNetwork;

import ru.gena.itmo.SocialNetwork.SocialNetwork.content.PatternsTree;
import ru.gena.itmo.SocialNetwork.SocialNetwork.content.TreesElement;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

public class Designer {

    public Designer(){}

    public String createSVGtoPatternsTree(PatternsTree tree){
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
                    svg.append("<text x=\"");
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

    public String toNeddedForm(String i){
        int id = Integer.parseInt(i);
        int useFullId = Integer.parseInt(i);
        int numberOfNumerals = 0;
        while (id / 10 != 0){
            id /= 10;
            numberOfNumerals++;
        }
        return "00000".substring(numberOfNumerals) + useFullId;
    }

    public String textAnalysis(String text){
        int siteswapStart = text.indexOf('&');
        int siteswapEnd = text.indexOf('&', siteswapStart + 1);
        if (siteswapStart != -1 && siteswapEnd != -1){
            String str = text.substring(siteswapStart + 1, siteswapEnd);
            ArrayList<Integer> siteswap = analysisSiteswap(str);
            if (siteswap != null){
                return visualize(siteswap);
            }
        }
        return text;
    }

    private String visualize(ArrayList<Integer> siteswap){
        StringBuilder animation = new StringBuilder("<style>\n");
        int maxThrow = siteswap.get(0);
        int numberOfBalls = siteswap.get(1);
        int neededTime = siteswap.get(2);
        int pos = 2;
        animationBall(animation,
                1,
                siteswap,
                pos,
                maxThrow);
        animation.append("</style>\n");/*
        animation.append("<svg xmlns=\"http://www.w3.org/2000/svg\" version=\"1.1\"\n" +
                "viewBox=\"0 0 500 500\" preserveAspectRatio=\"xMaxYMax none\">\n");
        int ballNumber = 0;
        while (i < siteswap.size()) {
            animation.append("<circle cx=\"50%\" cy=\"90\" r=\"10\" fill=\"red\" style=\"\n animation: ");
            int aDelay = ballNumber;
            int thisBallN = ballNumber;
            int ballsThrows = siteswap.get(i);
            i++;
            pos = i;
            while (i < ballsThrows + pos) {
                animation.append(addAnimationToBall(siteswap.get(i), thisBallN, aDelay));
                aDelay += siteswap.get(i);
                thisBallN += siteswap.get(i) % 2;
                i++;
                if (i < ballsThrows + pos) animation.append(", ");
            }
            animation.append("\"></circle>\n");
            ballNumber++;
        }
        animation.append("</svg>\n");//*/
        return animation.toString();
    }

    private String addAnimationToBall(int typeOfThrow, int ballNumber, int aDelay){
        StringBuilder animation = new StringBuilder();
        if (ballNumber % 2 == 0){
            animation.append("throwR");
        }else{
            animation.append("throwL");
        }
        animation.append(typeOfThrow);
        animation.append(" ");
        animation.append(typeOfThrow);
        animation.append("s");
        animation.append(" ");
        animation.append(aDelay);
        animation.append("s");

        return animation.toString();
    }
/*
    private String animationThrow(int typeOfThrow, int maxThrow, char side){
        StringBuilder animation = new StringBuilder((side == 'l')? "@keyframes throwL" : "@keyframes throwR");
        int startX = (side == 'l')? 100 : 400;
        int startY = 450;
        animation.append(typeOfThrow);
        animation.append(" {\nfrom {\ncx: ");//========================== from
        animation.append(startX);
        animation.append(";\ncy: ");
        animation.append(startY);
        animation.append(";\nanimation-timing-function: ease-out;\n}\n");
        animation.append("50% {\ncy: ");//================================ 50%
        animation.append(startY - 400 / maxThrow * typeOfThrow);
        animation.append(";\nanimation-timing-function: ease-in;\n}\n");
        animation.append("to {\ncx: ");//======================= to
        if (typeOfThrow % 2 == 0){
            animation.append(startX);
        }else{
            animation.append(500 - startX);
        }
        animation.append(";\ncy: ");
        animation.append(startY);
        animation.append(";\n}\n}\n");
        return animation.toString();
    }//*/

    private void animationBall(StringBuilder anim,
                               int ballNumber,
                               List<Integer> siteswap,
                               int pos,
                               int maxThrow){
        anim.append(siteswap.toString());
        anim.append("\n@keyframes ball"); anim.append(ballNumber);
        anim.append(" {\n");
        int keyframe = 0;
        int x = (ballNumber % 2 == 0)? 100 : 400;
        addKeyframesForOneCircle(anim,
                keyframe,
                x,
                siteswap,
                pos,
                maxThrow);
        if (siteswap.get(pos) % 2 != 0){
            addKeyframesForOneCircle(anim,
                    keyframe,
                    x,
                    siteswap,
                    pos,
                    maxThrow);
        }
        anim.append("100% {");
        anim.append("\ncx:"); anim.append(x);
        anim.append(";\ncy:"); anim.append(450);
        anim.append(";\n}\n}\n");
    }

    private void addKeyframesForOneCircle(StringBuilder anim,
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
    }

    private void addKeyframesToOneThrow(StringBuilder anim,
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
    private ArrayList<Integer> analysisSiteswap(String siteswap){
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

    private ArrayList<Integer> doSiteswapForBall(int[] siteswap, int siteswapLength, int i){
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

    private int getNumberOfBalls(int[] siteswap, int siteswapLength){
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

    private Integer getMaxThrow(int[] siteswap, int siteswapLength){
        int maxThrow = 0;
        for (int i = 0; i < siteswapLength; i++){
            maxThrow = (maxThrow < siteswap[i])? siteswap[i] : maxThrow;
        }
        return maxThrow;
    }

    private boolean checkSiteswap(int[] siteswap, int siteswapLength){
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

    private int[] stringToNumerals(String siteswap){
        int l = siteswap.length();
        int[] ans = new int[l];
        for(int i = 0; i < l; i++){
            char c = siteswap.charAt(i);
            int value = charToNumber(c);
            ans[i] = value;
        }
        return ans;
    }

    private int charToNumber(char c){
        if (c >= 'a' && c <= 'z'){
            return (int)c - (int)'a' + 1;
        }else if (c >= '0' && c <= '9'){
            return (int)c - (int)'0';
        }else{
            return -1;
        }
    }
}
