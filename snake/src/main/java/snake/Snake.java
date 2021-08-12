/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snake;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JFrame;
import java.awt.Toolkit;
import java.awt.event.*;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JPanel;

public class Snake extends JFrame {

    int width = 640;
    int height = 480;
    Point snake;
    Point comida;
    
    boolean gameOver = false;
    
    ArrayList<Point> lista;
    int widthPoint = 10;
    int heightPoint = 10;
    ImagenSnake imagenSnake;
    int direccion = KeyEvent.VK_LEFT;
    long frecuencia = 30;

    public Snake() {
        setTitle("snake");

        setSize(width, height);
        //posicionamos la ventana en la mitad de la pantalla
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - width / 2, dim.height / 2 - height / 2);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Teclas teclas = new Teclas();
        this.addKeyListener(teclas);

        startGame();

        imagenSnake = new ImagenSnake();
        this.getContentPane().add(imagenSnake);

        setVisible(true);

        Momento momento = new Momento();
        Thread trid = new Thread(momento);
        trid.start();
    }

    public void startGame() {
        comida = new Point(200, 200);
        snake = new Point(width / 2, height / 2);

        lista = new ArrayList<Point>();
        lista.add(snake);
        crearComida();
    }

    public void crearComida() {
        Random rnd = new Random();

        comida.x = rnd.nextInt(width);
        if ((comida.x % 5) > 0) {
            comida.x = comida.x - (comida.x % 5);
        }

        if (comida.x < 5) {
            comida.x = comida.x + 10;
        }

        comida.y = rnd.nextInt(height);
        if ((comida.y % 5) > 0) {
            comida.y = comida.y - (comida.y % 5);
        }

        if (comida.y < 5) {
            comida.y = comida.y + 10;
        }
    }

    public static void main(String[] args) throws Exception {

        Snake s = new Snake();

    }

    public void actualizar() {
        imagenSnake.repaint();
        lista.add(0, new Point(snake.x, snake.y));
        lista.remove((lista.size() - 1));

        for (int i = 1; i < lista.size(); i++) {
            Point punto = lista.get(i);
            if (snake.x == punto.x && snake.y == punto.y) {
                gameOver = true;
            }
        }

        if ((snake.x > (comida.x - 10)) && (snake.x < (comida.x + 10)) && (snake.y > (comida.y - 10)) && (snake.y > (comida.y - 10))) {
            lista.add(0, new Point(snake.x, snake.y));
            crearComida();
        }
    }

    public class ImagenSnake extends JPanel {

        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            g.setColor(new Color(0, 0, 255));
            g.fillRect(snake.x, snake.y, widthPoint, heightPoint);
            for (int i = 0; i < lista.size(); i++) {
                Point point = (Point) lista.get(i);
                g.fillRect(point.x, point.y, widthPoint, heightPoint);

            }

            g.setColor(new Color(255, 0, 0));
            g.fillRect(comida.x, comida.y, widthPoint, heightPoint);
            
            if(gameOver){
                g.drawString("GAME OVER", 200, 320);
            }
        }
    }

    public class Teclas extends KeyAdapter {

        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                System.exit(0);
            } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                if (direccion != KeyEvent.VK_DOWN) {
                    direccion = KeyEvent.VK_UP;
                }
            } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                if (direccion != KeyEvent.VK_UP) {
                    direccion = KeyEvent.VK_DOWN;
                }

            } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                if (direccion != KeyEvent.VK_RIGHT) {
                    direccion = KeyEvent.VK_LEFT;
                }

            } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                if (direccion != KeyEvent.VK_LEFT) {
                    direccion = KeyEvent.VK_RIGHT;
                }
            }

        }
    }

    public class Momento extends Thread {

        long last = 0;

        public void run() {
            while (true) {
                if ((java.lang.System.currentTimeMillis() - last) > frecuencia) {

                    if (!gameOver) {

                        if (direccion == KeyEvent.VK_UP) {
                            snake.y = snake.y - heightPoint;
                            if (snake.y < 0) {
                                snake.y = height - heightPoint;
                            }
                            if (snake.y > height) {
                                snake.y = 0;
                            }
                        } else if (direccion == KeyEvent.VK_DOWN) {
                            snake.y = snake.y + heightPoint;
                            if (snake.y < 0) {
                                snake.y = height - heightPoint;
                            }
                            if (snake.y > height) {
                                snake.y = 0;
                            }
                        } else if (direccion == KeyEvent.VK_LEFT) {
                            snake.x = snake.x - widthPoint;
                            if (snake.x < 0) {
                                snake.x = width - widthPoint;
                            }
                            if (snake.x > width) {
                                snake.x = 0;
                            }
                        } else if (direccion == KeyEvent.VK_RIGHT) {
                            snake.x = snake.x + widthPoint;
                            if (snake.x < 0) {
                                snake.x = width - widthPoint;
                            }
                            if (snake.x > width) {
                                snake.x = 0;
                            }
                        }
                    }
                    actualizar();
                    last = java.lang.System.currentTimeMillis();

                }
            }
        }

    }
}
