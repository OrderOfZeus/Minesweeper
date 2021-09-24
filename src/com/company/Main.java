package com.company;

public class Main {

    public static void main(String[] args) {
	package com.javarush.games.minesweeper;

        public class GameObject {
            public int x;
            public int y;
            public boolean isMine;
            public boolean isOpen;
            public boolean isFlag;
            public int countMineNeighbors;

            GameObject(int x, int y, boolean isMine) {
                this.x = x;
                this.y = y;
                this.isMine = isMine;
            }
        }

        package com.javarush.games.minesweeper;

import com.javarush.engine.cell.*;

import java.util.ArrayList;
import java.util.List;

        public class MinesweeperGame extends Game {
            private static final int SIDE = 9;
            private GameObject[][] gameField = new GameObject[SIDE][SIDE];
            private int countMinesOnField;
            private int countFlags;
            private int score;
            private int countClosedTiles = SIDE*SIDE;
            private static final String MINE = "\uD83D\uDCA3";
            private static final String FLAG =  "\uD83D\uDEA9";
            private boolean isGameStopped;


            @Override
            public void onMouseLeftClick(int y,int x){
                if (isGameStopped) {
                    restart();
                    return;
                }
                openTile(x,y);
            }

            @Override
            public void onMouseRightClick(int y, int x) {
                markTile(y,x);
            }

            @Override
            public void initialize() {
                setScreenSize(SIDE, SIDE);
                createGame();
            }

            private void createGame() {
                for (int y = 0; y < SIDE; y++) {
                    for (int x = 0; x < SIDE; x++) {
                        boolean isMine = getRandomNumber(10) < 1;
                        if (isMine) {
                            countMinesOnField++;
                        }
                        gameField[y][x] = new GameObject(x, y, isMine);
                        setCellColor(x, y, Color.ORANGE);
                        setCellValue(x,y,"");
                    }
                }
                countMineNeighbors();
                countFlags = countMinesOnField;
            }

            private List<GameObject> getNeighbors(GameObject gameObject) {
                List<GameObject> result = new ArrayList<>();
                for (int y = gameObject.y - 1; y <= gameObject.y + 1 ; y++) {
                    for (int x = gameObject.x - 1; x <= gameObject.x + 1 ; x++) {
                        if (y < 0 || y >= SIDE) {
                            continue;
                        }
                        if (x < 0 || x >= SIDE) {
                            continue;
                        }
                        if (gameField[y][x] == gameObject) {
                            continue;
                        }
                        result.add(gameField[y][x]);
                    }
                }
                return result;
            }

            private void countMineNeighbors() {
                for (int y = 0; y < SIDE; y++) {
                    for (int x = 0; x < SIDE; x++) {
                        GameObject gameObject = gameField[y][x];
                        if (!gameObject.isMine) {
                            for (GameObject neighbor : getNeighbors(gameObject)) {
                                if (neighbor.isMine) {
                                    gameObject.countMineNeighbors++;
                                }
                            }
                        }
                    }
                }
            }
            private void restart() {
                countClosedTiles = SIDE*SIDE;
                score = 0;
                setScore(score);
                countMinesOnField = 0;
                isGameStopped = false;
                createGame();
            }

            private void openTile(int x, int y) {
                GameObject gameObject = gameField[y][x];
                if (gameObject.isOpen || gameObject.isFlag || isGameStopped) {
                    return;
                }
                gameObject.isOpen = true;
                countClosedTiles--;
                setCellColor(x,y,Color.GREEN);
                if(gameObject.isMine){
                    setCellValueEx(gameObject.x,gameObject.y,Color.RED, MINE);
                    gameOver();
                    return;
                }
                else if (gameObject.countMineNeighbors==0){
                    setCellValue(gameObject.x,gameObject.y,"");
                    List<GameObject> neighbors = getNeighbors(gameObject);
                    for (GameObject neighbor : neighbors){
                        if (!neighbor.isOpen){
                            openTile(neighbor.x,neighbor.y);
                        }
                    }
                }
                else {
                    setCellNumber(x,y,gameObject.countMineNeighbors);
                }

                score += 5;
                setScore(score);

                if (countClosedTiles==countMinesOnField) {
                    win();
                }
            }

            private void markTile(int x, int y) {
                GameObject gameObject = gameField[y][x];
                if (gameObject.isOpen || isGameStopped || (countFlags == 0 && !gameObject.isFlag)) {
                    return;
                }
                if (gameObject.isFlag) {
                    countFlags++;
                    gameObject.isFlag = false;
                    setCellValue(x,y,"");
                    setCellColor(x,y, Color.ORANGE);
                }
                else {
                    countFlags--;
                    gameObject.isFlag = true;
                    setCellValue(x,y,FLAG);
                    setCellColor(x,y,Color.YELLOW);
                }
            }

            private void gameOver() {
                showMessageDialog(Color.WHITE, "GAME OVER", Color.BLACK, 50);
                isGameStopped = true;
            }

            private void win() {
                showMessageDialog(Color.WHITE, "YOU WIN", Color.BLACK, 50);
                isGameStopped = true;
            }
        }


    }
}
