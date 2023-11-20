package Odev4;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;

public class Surface extends JPanel implements ActionListener {

// İki resmin hareketini sağlayan JPanel sınıfı

    private final int DELAY = 15; // Animasyonun yeniden çizilme hızı
    private final int INITIAL_DELAY = 1000;

    private Timer timer;


    // Resimlerimizin merkez noktaları
    private double cannonX;
    private double cannonY;
    private double whellX;
    private double whellY;
    private double cannonBallX;
    private double cannonBallY;

    //yer çekimi ivmesi : 9.8 olarak alınabilir hareketin daha net gzükmesi açısından 1 olarak alıyorum
    private double gravity = 1;

    // Size değerleri
    private int cannonWidth = 90;
    private int cannonHeight = 90;
    private int whellWidth = 40;
    private int whellHeight = 40;
    private int cannonBallWidth = 25;
    private int cannonBallHeight = 25;

    // çimen uzunluğumuz
    private int grassHeight = 90;

    // Açılar
    private double launchAngle = 45; // fırlatma açısı
    private double cannonStartAngle = Math.toRadians(24.5); // top arabasını düzlüyoruz
    private double cannonAngle = cannonStartAngle; // top arabasının açısı
    private double cannonBallAngle = Math.toRadians(0); // mermimizin açısı
    private double getLaunchAngle = cannonStartAngle + Math.toRadians(-launchAngle);
    private double whellAngle = 0;

    private double cannonSpeed = 1; // Hareket hızı
    private double cannonBallSpeed = 40.5; // merminin başlangıç hızı

    private double cannonBallSpeedX;   // Yatay eksendeki hız bileşeni
    private double cannonBallSpeedY;   // Dikey eksendeki hız bileşeni

    private Image imgCannon;        // *
    private Image imgWhell;         // Resim Nesnelerim
    private Image imgCannonBall;    // *

    // Constructor
    public Surface() {
        initTimer();
        loadImage();
        calculateVelocities();
        // Güllenin düşme süresini hesapla
        double fallTime = calculateFallTime();
        System.out.println("Gülle yere düştü! Düşme Süresi: " + fallTime + " saniye");
    }

    // Animasyon için timer'ı başlat
    private void initTimer() {
        timer = new Timer(DELAY, this);
        timer.setInitialDelay(INITIAL_DELAY);
        timer.start();
    }
    // Güllemizin açıya göre x ve y eksenindeki hızını hesaplıyorudz
    private void calculateVelocities() {
        double radian = Math.toRadians(-launchAngle);
        cannonBallSpeedX = cannonBallSpeed * Math.cos(radian);
        cannonBallSpeedY = (-cannonBallSpeed * Math.sin(radian)); // negatif yönü yukarı olarak aldık
    }
    private double calculateFallTime() {
        // Tuçuş = 2*Vilk * sin(Angle)/ yerçekimi ivmesi
        // Topun başlangıç yüksekliği yüksekliği ve çim yüksekliği hesaba katılmamıştır
        return (2 * cannonBallSpeed*Math.sin(launchAngle)/ gravity);
    }
    // Nesneler için resimleri yükle ve boyutlandır
    private void loadImage() {
        imgCannon = new ImageIcon("/Users/kemal/Desktop/odev4/images/cannon.png").getImage();
        imgWhell = new ImageIcon("/Users/kemal/Desktop/odev4/images//wheel.png").getImage();
        imgCannonBall = new ImageIcon("/Users/kemal/Desktop/odev4/images/round2.png").getImage();

        imgCannon = imgCannon.getScaledInstance(cannonWidth, cannonHeight, Image.SCALE_SMOOTH);
        imgWhell = imgWhell.getScaledInstance(whellWidth, whellHeight, Image.SCALE_SMOOTH);
        imgCannonBall = imgCannonBall.getScaledInstance(cannonBallWidth, cannonBallHeight, Image.SCALE_SMOOTH);
    }

    // nesnelerin başlangıç pozisyonları
    private void setInitialPositions() {
        int canonAndWhelDiffrent = 15;

        int padding = 9;

        cannonX = -80;
        cannonY = getHeight() - cannonHeight + padding - grassHeight;

        whellX = cannonX + canonAndWhelDiffrent;
        whellY = getHeight() - whellHeight - grassHeight;

        cannonBallX = 82;
        cannonBallY = whellY - cannonBallHeight/2 + 3;
    }

    private void doDrawing(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();

        g2d.setPaint(new Color(55, 124, 182, 69));
        g2d.fillRect(0, 0, getWidth(),  getHeight() - grassHeight);
        g2d.setPaint(Color.green);
        g2d.fillRect(0, getHeight() - grassHeight, getWidth(),  grassHeight);


        if(whellX > 80) {
            //Gülleyi çizer ve top arabası içersinde gülleyi döndürür
            rotateAndDrawImg(imgCannonBall, cannonBallAngle, cannonBallX, cannonBallY,cannonBallWidth / 2, cannonBallHeight-4, g2d);

        }

        // top arabasını çizer ve açısını ayarlar
        rotateAndDrawImg(imgCannon, cannonAngle, cannonX, cannonY,34.6, 60.3, g2d);
        // Tekerleği çizer ve döndürür
        rotateAndDrawImg(imgWhell, whellAngle, whellX, whellY,whellWidth / 2, whellHeight / 2, g2d);



        g2d.dispose();
    }

    public void rotateAndDrawImg(Image img, double angle, double imgX, double imgY, double anchorX, double anchorY, Graphics2D g2d ) {
        AffineTransform transform = new AffineTransform(); // Sınıfın Açıklaması
        transform.translate(imgX, imgY); // X ve Y konumları
        transform.rotate(angle, anchorX, anchorY);  // Resmimizin hangi noktasından kaç derece döndüreceğimizi belirtir
        g2d.drawImage(img, transform, null);    // Resmimizi çizer
    }

    public void cannonMove() {
        // ileri gitme
        cannonX += cannonSpeed;
        //durma
        if (cannonX > 65) {
            cannonX = 66;
        }
        //  girdiğimiz açı değerine göre top arabasının kendini ayarlama hareketi
        if (whellX > 80) {
            cannonAngle -= Math.toRadians(1);
            cannonBallAngle -= Math.toRadians(1);
            if(cannonAngle <= getLaunchAngle) {
                cannonAngle = getLaunchAngle;
                cannonBallAngle = getLaunchAngle - cannonStartAngle;
            }
        }
    }
    public void  whellMove(){
        //  ileri gitme
        whellX += cannonSpeed;
        //  dönme
        whellAngle += Math.toRadians(2);
        // dönme ve ileri gitme hareketini durdurma
        if (whellX > 80) {
            whellAngle = 290;
            whellX = 81;
        }
    }
    public void cannonBallMove() {
        // x ve y ekseni hareketi
        if (Math.toDegrees(cannonAngle) <= Math.toDegrees(getLaunchAngle) && cannonX > 65) {
            cannonBallX += cannonBallSpeedX;
            cannonBallY -= cannonBallSpeedY;
            cannonBallSpeedY -= gravity;
        }

        // Gülle yere düştü mü kontrol et
        if (cannonBallY >= getHeight() - cannonBallHeight - grassHeight) {
            cannonBallY = getHeight() - cannonBallHeight - grassHeight;
            cannonBallSpeedY = 0;
            cannonBallSpeedX = 0;
        }
    }





    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }
    private void moveObjects() {
        cannonMove();
        whellMove();
        cannonBallMove();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        moveObjects();
        repaint();
    }

    @Override
    public void invalidate() {
        super.invalidate();
        setInitialPositions();

    }
}




