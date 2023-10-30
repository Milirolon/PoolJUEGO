import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class ColisionDeBolas extends JFrame {

    private static final int WIDTH = 752;
    private static final int HEIGHT = 400;
    private static final int BALL_RADIUS = 15;
    private static final double INITIAL_SPEED = 4.0;
    private static final double FRICTION = 0.99;

    private Ball ball1, ball2;
    private JLabel backgroundLabel;

    public ColisionDeBolas() {
    	
    	  MensajeAyuda();
        setTitle("Colisión de Bolas");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        // Crear el fondo como un JLabel
        backgroundLabel = new JLabel();
        backgroundLabel.setIcon(new ImageIcon("file:///C:/Users/Alumno/eclipse-workspace/PoolJUEGO/src/pool.jpeg"));
        backgroundLabel.setBounds(0, 0, WIDTH, HEIGHT);
        backgroundLabel.setOpaque(true);
        backgroundLabel.setBackground(Color.GREEN);
        getContentPane().add(backgroundLabel);

        // Crear las bolas y añadir las bolas sobre el fondo
        ball1 = new Ball(100, 300, BALL_RADIUS, Color.WHITE, 0, 0);
        ball2 = new Ball(700, 300, BALL_RADIUS, Color.RED, 0, 0);
        backgroundLabel.add(ball1);
        backgroundLabel.add(ball2);

        setFocusable(true);

        // Hilo de animación
        Thread animationThread = new Thread(() -> {
            while (true) {
                ball1.move();
                ball2.move();
                checkCollision();
                repaint();
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        animationThread.start();

        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });
    }

    private void formKeyPressed(java.awt.event.KeyEvent evt) {
        int keyCode = evt.getKeyCode();
        if (keyCode == KeyEvent.VK_LEFT) {
            ball1.setSpeed(-INITIAL_SPEED, ball1.speedY);
        }
        if (keyCode == KeyEvent.VK_RIGHT) {
            ball1.setSpeed(INITIAL_SPEED, ball1.speedY);
        }
        if (keyCode == KeyEvent.VK_UP) {
            ball1.setSpeed(ball1.speedX, -INITIAL_SPEED);
        }
        if (keyCode == KeyEvent.VK_DOWN) {
            ball1.setSpeed(ball1.speedX, INITIAL_SPEED);
        }
        // Movimiento para la pelota roja (ball2)
        if (keyCode == KeyEvent.VK_A) {
            ball2.setSpeed(-INITIAL_SPEED, ball2.speedY);
        }
        if (keyCode == KeyEvent.VK_D) {
            ball2.setSpeed(INITIAL_SPEED, ball2.speedY);
        }
        if (keyCode == KeyEvent.VK_W) {
            ball2.setSpeed(ball2.speedX, -INITIAL_SPEED);
        }
        if (keyCode == KeyEvent.VK_S) {
            ball2.setSpeed(ball2.speedX, INITIAL_SPEED);
        }
    }


    private void checkCollision() {
    	 double distance = Math.sqrt(Math.pow(ball1.x - ball2.x, 2) + Math.pow(ball1.y - ball2.y, 2));
    	    if (distance <= 2 * BALL_RADIUS) {
    	        double angle = Math.atan2(ball2.y - ball1.y, ball2.x - ball1.x);
    	        double speed1 = Math.sqrt(ball1.speedX * ball1.speedX + ball1.speedY * ball1.speedY);
    	        double speed2 = Math.sqrt(ball2.speedX * ball2.speedX + ball2.speedY * ball2.speedY);
    	        double direction1 = Math.atan2(ball1.speedY, ball1.speedX);
    	        double direction2 = Math.atan2(ball2.speedY, ball2.speedX);

    	        double newDirection1 = angle + Math.PI + (direction1 - angle);
    	        double newDirection2 = angle + (direction2 - angle);

    	        double newSpeed1 = speed2;
    	        double newSpeed2 = speed1;

    	        ball1.setSpeed(newSpeed1 * Math.cos(newDirection1), newSpeed1 * Math.sin(newDirection1));
    	        ball2.setSpeed(newSpeed2 * Math.cos(newDirection2), newSpeed2 * Math.sin(newDirection2));
    	    }

    	    // Verificar colisiones con las paredes y cambiar la dirección
    	    if (ball1.x - BALL_RADIUS <= 0 || ball1.x + BALL_RADIUS >= WIDTH-30) {
    	        ball1.speedX = -ball1.speedX;
    	    }
    	    if (ball1.y - BALL_RADIUS <= 0 || ball1.y + BALL_RADIUS >= HEIGHT-30) {
    	        ball1.speedY = -ball1.speedY;
    	    }

    	    if (ball2.x - BALL_RADIUS <= 0 || ball2.x + BALL_RADIUS >= WIDTH-30) {
    	        ball2.speedX = -ball2.speedX;
    	    }
    	    if (ball2.y - BALL_RADIUS <= 0 || ball2.y + BALL_RADIUS >= HEIGHT-30) {
    	        ball2.speedY = -ball2.speedY;
    	    }
            ball1.speedX *= FRICTION;
            ball1.speedY *= FRICTION;
            ball2.speedX *= FRICTION;
            ball2.speedY *= FRICTION;
        }

    private void MensajeAyuda() {
    	String message = "INSTRUCCIONES:\n\n" +
                "Antes de empezar, posiciona la bola roja donde quieras en la mesa.\n\n" +
                "- Mover: Teclas W-A-S-D\n\n" +
                "Jugador 1 (Bola blanca):\n" +
                "- Mover: Flechas\n" +
                "- Puedes pegar a la primera bola desde diferentes ángulos usando las flechas.\n\n" +
                "Reglas adicionales:\n" +
                "- Al chocar contra las paredes, las bolas cambian de dirección.\n" +
                "- Las bolas disminuyen su velocidad a medida que se mueven debido al rozamiento en la mesa.\n\n" +
                "Objetivo:\n" +
                "Simular un juego de colisión entre dos bolas parecido al Pool.";

        JOptionPane.showMessageDialog(this, message, "AYUDA", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private class Ball extends JComponent {
        double x, y;
        int radius;
        Color color;
        double speedX, speedY;

        public Ball(double x, double y, int radius, Color color, double speedX, double speedY) {
            this.x = x;
            this.y = y;
            this.radius = radius;
            this.color = color;
            this.speedX = speedX;
            this.speedY = speedY;
            setBounds((int) (x - radius), (int) (y - radius), 2 * radius, 2 * radius);
        }

        public void setSpeed(double speedX, double speedY) {
            this.speedX = speedX;
            this.speedY = speedY;
        }

        public void move() {
            x += speedX;
            y += speedY;
            setBounds((int) (x - radius), (int) (y - radius), 2 * radius, 2 * radius);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(color);
            g.fillOval(0, 0, getWidth(), getHeight());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ColisionDeBolas simulation = new ColisionDeBolas();
            simulation.setVisible(true);
        });
    }
}
