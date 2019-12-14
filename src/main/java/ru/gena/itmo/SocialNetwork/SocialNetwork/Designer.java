package ru.gena.itmo.SocialNetwork.SocialNetwork;

import ru.gena.itmo.SocialNetwork.SocialNetwork.content.PatternsTree;
import ru.gena.itmo.SocialNetwork.SocialNetwork.content.TreesElement;
import sun.misc.Queue;

import java.util.ArrayDeque;
import java.util.ArrayList;

public class Designer {

    public Designer(){}

    public String createSVGtoPatternsTree(PatternsTree tree){
        StringBuilder svg = new StringBuilder("<svg xmlns=\"http://www.w3.org/2000/svg\" version=\"1.1\"\n" +
                "viewBox=\"0 0 500 500\" preserveAspectRatio=\"xMaxYMax none\">\n");

        //реализовать проход в глубину
        boolean[] used = new boolean[tree.getNumberOfElements()];
        ArrayDeque<ArrayList<TreesElement>> queue = new ArrayDeque<>();

        queue.add(tree.getLine(0));
        ArrayList<TreesElement> v;
        used[0] = true;

        int n = 0;
        int row = 1;
        int ySpace = 20;
        while (!queue.isEmpty()){
            v = queue.pop();//берем следующую в очереди вершину
            for (int i = 1; i < v.size(); i++){//идем по всем связанным элементам
                int w = v.get(i).id;
                int elementsInRow = 5;//(v.size() > 7)? 6 : v.size() - 1;
                String name = v.get(i).name;
                if (!used[w]){//проверяем посещали ли мы его
                    used[w] = true;//если нет, отмечаем как посещенного
                    int x2 = 100 / (2 * elementsInRow) + 100 / elementsInRow * n;//определяем новые координаты
                    int y2 = 40 * row;
                    if (w < tree.getNumberOfLines()){
                        tree.getLine(w).get(0).x = x2;//сохраняем координаты
                        tree.getLine(w).get(0).y = y2;
                        queue.add(tree.getLine(w));//добовляем в очередь элемент
                    }
                    svg.append("<line x1=\"" + v.get(0).x + "%\" " + //заполняем svg
                            "y1=\"" + (v.get(0).y + ySpace) + "\" " +
                            "x2=\"" + x2 + "%\" " +
                            "y2=\"" + y2 + "\" " +
                            "stroke=\"black\"></line>\n" +
                            "<text x=\"" + (x2 - name.length()/2) +
                            "%\" y=\"" + (y2 + ySpace / 2) +
                            "\"><a href=\"/pattern?id=" + w +
                            "\">" + name + //здесь должа быть ссылка на страницу паттерна и его название
                            "</a></text>\n");
                    row = row + (n + 1) / elementsInRow;
                    n = (n + 1) % elementsInRow;
                }
            }
            row += 1;
            n = 0;
        }

        svg.append("</svg>\n");
        return svg.toString();
    }
}
