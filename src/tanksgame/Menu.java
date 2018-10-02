package tanksgame;

import engine.Drawable;
import processing.core.PApplet;
import processing.core.PConstants;

import java.util.*;

public class Menu implements Drawable {
    private final PApplet parent;
    private String currentPage = "main";
    private final Map<String, ArrayList<MenuBox>> pages = new HashMap<>();

    void handleMouseClick() {
        for(MenuBox menuBox : pages.get(currentPage)) {
            if(menuBox.mouseInBox()){
                menuBox.click();
            }
        }
    }

    void updateMenuEntry(String newText, String page, String oldText) {
        if(pages.containsKey(page)) {
            for(MenuBox menuBox : pages.get(page)) {
                if(menuBox.getId().equals(oldText)) {
                    menuBox.setText(newText);
                }
            }
        }
    }


    Menu(PApplet parent) {
        this.parent = parent;
    }

    Menu(PApplet parent, String currentPage) {
        this.parent = parent;
        this.currentPage = currentPage;
    }

    public void addMenuBox(String pageName, MenuBox menuBox) {
        if(pages.containsKey(pageName)) {
            pages.get(pageName).add(menuBox);
        } else {
            pages.put(pageName, new ArrayList<>(Collections.singletonList(menuBox)));
        }


    }

    public void draw() {
        parent.background(135, 206, 235);
//        parent.background(100, 100, 100);
        drawPage(currentPage);
    }


    private void drawPage(String pageName) {
        parent.textAlign(PConstants.CENTER, PConstants.CENTER);
        parent.rectMode(PConstants.CENTER);
        parent.textSize(parent.height/20f);

        parent.text("Tanks", parent.width/2f, parent.height/6f);

        for (MenuBox menuBox : pages.get(pageName)) {
            menuBox.draw();
        }
    }

    public void changePage(String page) throws InvalidPageException {
        if(pages.containsKey(page)){
            currentPage = page;
        } else {
            throw new InvalidPageException("Page not found");
        }
    }
}
