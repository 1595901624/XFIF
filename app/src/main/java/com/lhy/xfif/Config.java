package com.lhy.xfif;

/***
 *  自定义配置文件对象
 *
 *  @author lhy 2018年5月17日10:35:22 https://github.com/1595901624/xfif
 *
 */

public class Config {
    /* 判断xposed是否工作 */
    private boolean isOpen;
    /* 当前要修改的分数 */
    private int score;

    public Config(){

    }

    public Config(boolean isOpen, int score){
        this.isOpen = isOpen;
        this.score = score;
    }

    /****************Setter Or Getter***********************/
    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}