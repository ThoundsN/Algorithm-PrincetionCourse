

import edu.princeton.cs.algs4.Picture;

import java.awt.Color;


public class SeamCarver {
    private int width;
    private int height;
    private double[][] energy;
    private int[] vseam,hseam;
    private int[][] rgb;



    public SeamCarver(Picture picture){ ;
    if (picture == null) throw new IllegalArgumentException();
        this.width = picture.width();
        this.height = picture.height();
        this.rgb = new int[width][height];
        for (int i = 0; i< width; i++){
            for (int j = 0; j< height; j++){
                rgb[i][j] = picture.getRGB(i,j);
            }
        }
        this.energy = new double[width][height];
        caluateallenergy();


    }                // create a seam carver object based on the given picture


    public Picture picture(){
     Picture pic = new Picture(width,height);
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                pic.set(i, j, new Color(rgb[i][j], true));
        return pic;

    }                          // current picture
    public     int width(){
        return width;
    }                           // width of current picture
    public     int height(){
        return  height;
    }                           // height of current picture

    private void caluateallenergy(){
        double[][] energyarray = new double[width][height];
        for (int i = 0; i < width; i++){
            for (int j = 0; j< height; j++){
                energyarray[i][j] =  calculatespecificenergy(i,j);
            }
        }
        this.energy = energyarray;
    }

    private double calculatespecificenergy(int x, int y){
        if (x <= 0|| x>= width-1|| y <=0|| y>= height-1) return 1000;

        double a = calculateXenergy(x, y);
        double b = calculateYenergy(x, y);

        return Math.sqrt(a + b);

    }

    public  double energy(int x, int y){

        if (x< 0 ||x>= width||y<0 ||y>= height) throw new IllegalArgumentException();

        return energy[x][y];
    }               // energy of pixel at column x and row y

    private double  calculateXenergy(int x, int y){
        int rgbbefore = rgb[x-1][y];
        int r1 = (rgbbefore >> 16) & 0xFF;
        int g1 = (rgbbefore >>  8) & 0xFF;
        int b1 = (rgbbefore >>  0) & 0xFF;
        int rgbnow = rgb[x+1][y];
        int r2 = (rgbnow >> 16) & 0xFF;
        int g2 = (rgbnow >>  8) & 0xFF;
        int b2 = (rgbnow >>  0) & 0xFF;

        return (r1 - r2)*(r1 - r2)+(g1 - g2)*(g1 - g2)+(b1 - b2)*(b1 - b2);

    }
    private double calculateYenergy(int x, int y){
        int rgbbefore = rgb[x][y-1];
        int r1 = (rgbbefore >> 16) & 0xFF;
        int g1 = (rgbbefore >>  8) & 0xFF;
        int b1 = (rgbbefore >>  0) & 0xFF;
        int rgbnow = rgb[x][y+1];
        int r2 = (rgbnow >> 16) & 0xFF;
        int g2 = (rgbnow >>  8) & 0xFF;
        int b2 = (rgbnow >>  0) & 0xFF;

        return (r1 - r2)*(r1 - r2)+(g1 - g2)*(g1 - g2)+(b1 - b2)*(b1 - b2);

    }


    public   int[] findHorizontalSeam(){
        if (hseam!= null) return hseam;
        int[] seam = new int[width];

        int[][] edgeTo = new int[width][height];
        double[][] distTo = new double[width][height];

        for (int i = 1; i< width; i++){
            for (int j = 0; j< height; j++){
                distTo[i][j] = Double.POSITIVE_INFINITY;
            }
        }

        for (int i = 1; i< width; i++){
            for (int j = 0; j< height; j++){
                //down
                if (j>0 && distTo[i][j-1] > distTo[i-1][j] + energy[i][j-1]){
                    distTo[i][j-1]  =distTo[i-1][j] + energy[i][j-1];
                    edgeTo[i][j-1] = j;
                }
                //midium
                if (distTo[i][j] > distTo[i-1][j] + energy[i][j]){
                    distTo[i][j]  = distTo[i-1][j] + energy[i][j];
                    edgeTo[i][j] = j;
                }
                //up
                if (j<height-1 && distTo[i][j+1] > distTo[i-1][j] + energy[i][j+1]){
                    distTo[i][j+1]  = distTo[i-1][j] + energy[i][j+1];
                    edgeTo[i][j+1] = j;
                }
            }
        }

        seam[width-1] = 0;

        for(int j = 0; j< height; j++){
            if (distTo[width-1][seam[width-1]]> distTo[width-1][j]){
                seam[width-1]  = j;
            }
        }

        for (int i = width -1; i>0; i--){
            seam[i-1] = edgeTo[i][seam[i]];
        }

        hseam = seam;
        return seam.clone();

    }               // sequence of indices for horizontal seam

    public   int[] findVerticalSeam(){
        if (vseam!= null) return vseam;
        int[] seam = new int[height];

        int[][] edgeTo = new int[width][height];
        double[][] distTo = new double[width][height];

        for (int j = 1; j< height; j++){
            for (int i = 0; i< width; i++){
                distTo[i][j] = Double.POSITIVE_INFINITY;
            }
        }

        for (int j =1; j< height; j++){
            for (int i = 0;  i< width; i++){
                //left
                if (i>0 && distTo[i-1][j] > distTo[i][j-1] + energy[i-1][j]){
                    distTo[i-1][j]  = distTo[i][j-1] + energy[i-1][j];
                    edgeTo[i-1][j] = i;
                }
                //middle
                if (distTo[i][j] > distTo[i][j-1] + energy[i][j]){
                    distTo[i][j]  = distTo[i][j-1] + energy[i][j];
                    edgeTo[i][j] = i;
                }
                //right
                if (i<width-1 && distTo[i+1][j] > distTo[i][j-1] + energy[i+1][j]){
                    distTo[i+1][j]  = distTo[i][j-1] + energy[i+1][j];
                    edgeTo[i+1][j] = i;
                }
            }
        }

        seam[height-1] = 0;

        for(int i = 0; i< width; i++){
            if (distTo[seam[height-1]][height-1]> distTo[i][height-1]){
                seam[height-1]  = i;
            }
        }

        for (int j = height -1; j>0; j--){
            seam[j-1] = edgeTo[seam[j]][j];
        }

        vseam = seam;
        return seam.clone();
    }                 // sequence of indices for vertical seam


    public    void removeHorizontalSeam(int[] seam){
        if (seam == null) throw new IllegalArgumentException();
        if (seam.length != width) throw new IllegalArgumentException();
        for (int i = 0; i< seam.length; i++){
            if (seam[i]<0 || seam[i] > height -1 ) throw new IllegalArgumentException();
        }
        for (int i =0; i< seam.length-1; i++){
            if (Math.abs(seam[i+1]-seam[i])>1) throw new IllegalArgumentException();
        }
        int j;
        for (int i = 0; i < width; i++ ){
            j = seam[i];
            if (j == height -1)  {
                energy[i][height-2] = 1000;
                continue;
            }
            System.arraycopy(rgb[i],j+1 ,rgb[i],j, height-j-1 );
            System.arraycopy(energy[i],j+1 ,energy[i],j, height-j-1 );

            if (j ==0 ){
                energy[i][j] = 1000;
            }
        }
        height--;
        hseam = null;
        vseam = null;

        for ( int i = 1; i< width -1 ; i++){
            if (i>1) this.energy[i-1][seam[i]] = calculatespecificenergy(i-1 ,seam[i]);   //left
            if (i< width-2 )this.energy[i+1][seam[i]] = calculatespecificenergy(i+1,seam[i]);  //right
            if (seam[i]>1) this.energy[i][seam[i]-1] = calculatespecificenergy(i,seam[i]-1);  //up
            if (seam[i]< height-2) this.energy[i][seam[i]] = calculatespecificenergy(i,seam[i]);  //down
        }

    }   // remove horizontal seam from current picture

    public    void removeVerticalSeam(int[] seam){
        if (seam == null) throw new IllegalArgumentException();
        if (seam.length != height) throw new IllegalArgumentException();
        for (int i = 0; i< seam.length; i++){
            if (seam[i]<0 || seam[i] > width -1 ) throw new IllegalArgumentException();
        }
        for (int i =0; i< seam.length-1; i++){
            if (Math.abs(seam[i+1]-seam[i])>1) throw new IllegalArgumentException();
        }
        this.energy= transposeEnergy(this.energy);
        this.rgb = transposeRGB(this.rgb);
        exchangehw();
        removeHorizontalSeam(seam);
        this.energy= transposeEnergy(this.energy);
        this.rgb = transposeRGB(this.rgb);
        exchangehw();

    }     // remove vertical seam from current picture

    private double[][] transposeEnergy(double[][] array){
        int w = array.length;
        int h = array[0].length;
        double[][] newenergy = new double[h][w];
        for (int j =0 ; j< h ; j++){
            for (int i = 0; i< w ; i++){
                newenergy[j][i] = array[i][j];
            }
        }
        return newenergy;
    }

    private int[][] transposeRGB(int[][] array){
        int w = array.length;
        int h = array[0].length;
        int[][] newRGB = new int[h][w];
        for (int j =0 ; j< h ; j++){
            for (int i = 0; i< w ; i++){
                newRGB[j][i] = array[i][j];
            }
        }
        return newRGB;
    }

    private void exchangehw() {
        int tmp = this.height;
        this.height = width;
        this.width = tmp;
    }

}