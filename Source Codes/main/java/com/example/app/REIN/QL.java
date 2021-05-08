package com.example.app.REIN;





import java.util.Random;
/**
 *
 * @author dhanuka
 */
public class QL {

    private final double alpha = 0.1; // Learning rate
    private final double gamma = 0.9; // Eagerness - 0 looks in the near future, 1 looks in the distant future

    private final int QmatrixWidth = 3;
    private final int QmatrixHeight = 5;
    private final double [][] Qmatrix = new double [QmatrixHeight] [QmatrixWidth];

    private int currentBrightness = 0;
    private int userChangedBrightness = 0;

    private int []brightnessStates = new int[QmatrixHeight];



    //reward function
    private double reward(int currentBrightness, int userChangedBrightness){
        //return the reward
        double d= userChangedBrightness-currentBrightness;
        double value = 1/Math.abs(d);
        return round(value,2);
    }
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
    private int getState(double brightness){

        if(brightness>=0 && brightness<20)
            return 0;
        else if(brightness>=20 && brightness<40)
            return 1;
        else if(brightness>=40 && brightness<60)
            return 2;
        else if(brightness>=60 && brightness<80)
            return 3;
        else if(brightness>=80 && brightness<100)
            return 4;
        else
            return -1;
    }

    private void setInitialbrightness(){
        brightnessStates[0] = 10;
        brightnessStates[1] = 30;
        brightnessStates[2] = 50;
        brightnessStates[3] = 70;
        brightnessStates[4] = 90;
    }


    private void intQmatrix(){
        for(int i =0; i<QmatrixHeight; i++){
            for(int j=0; j<QmatrixWidth; j++)
                Qmatrix[i][j]= -1;
        }
    }

    private void printQ_Matrix(){
        System.out.println("\t     S | I | D |");
        System.out.println("-----------");
        for(int i =0; i<QmatrixHeight; i++){
            System.out.print("State : " + (i)*20 +"<=>"+ (i+1)*20 +" | ");
            for(int j=0; j<QmatrixWidth; j++)
                System.out.print(Qmatrix[i][j] + " | ");

            System.out.println("");
        }

    }

    private double maxQ(int state){
        double max=0;
        for(int i=0; i<2; i++){
            if(Qmatrix[state][i]>max)
                max=Qmatrix[state][i];
        }
        return max;
    }

    private void updateQmatrix(){

        int curIntBrightness=0;

        Random rand = new Random();

        for(int i=0; i<1000;i++){

            currentBrightness = rand.nextInt((99 - 0) + 1) + 0;

            userChangedBrightness = rand.nextInt((99 - 0) + 1) + 0;

            double r = reward(currentBrightness, userChangedBrightness);

            if(r<0.05)
                r= (-1);


            int cbState= getState(currentBrightness);


            int ucState= getState(userChangedBrightness);

            //action 0 -> stay
            //action 1 -> increase by one point
            //action 2 -> decrease by one point

            // Q(state,action)= Q(state,action) + alpha * (R(state,action) + gamma * Max(next state, all actions) - Q(state,action))

            if(currentBrightness==userChangedBrightness){
                //action 0 -> stay
                double currentQ = Qmatrix[cbState][0];
                double maxQ = maxQ(ucState);
                double newQvalue = currentQ + alpha*(r+gamma*maxQ -currentQ );
                Qmatrix[cbState][0] = round(newQvalue,2);

            }else if (currentBrightness<userChangedBrightness){
                //action 1 -> increase by one point
                double currentQ = Qmatrix[cbState][1];
                double maxQ = maxQ(ucState);
                double newQvalue = currentQ + alpha*(r+gamma*maxQ -currentQ );
                Qmatrix[cbState][1] = round(newQvalue,2);
                brightnessStates[cbState] += 1;


            }else if(currentBrightness>userChangedBrightness){
                //action 2 -> decrease by one point
                double currentQ = Qmatrix[cbState][2];
                double maxQ = maxQ(ucState);
                double newQvalue = currentQ + alpha*(r+gamma*maxQ -currentQ );
                Qmatrix[cbState][2] = round(newQvalue,2);
                brightnessStates[cbState] -= 1;

            }

            System.out.println("CB : " + currentBrightness + " | " + "UB :" + userChangedBrightness);

            System.out.println("------------");
            printQ_Matrix();
            System.out.println("------------");


        }

        double max=0;
        for(int i=0; i<QmatrixHeight;i++){
            for(int j=0; j<QmatrixWidth;j++)
                if(max<Qmatrix[i][j])
                    max=Qmatrix[i][j];
        }

        for(int i=0; i<QmatrixHeight;i++){
            for(int j=0; j<QmatrixWidth;j++)
                Qmatrix[i][j] =  round(Qmatrix[i][j]/max,2);
        }

    }

    public static void main(String[] args) {
        QL q = new QL();
        q.intQmatrix();
        q.printQ_Matrix();
        q.updateQmatrix();
        q.printQ_Matrix();
    //    lineGraph g = new lineGraph();
     //   g.launch(lineGraph.class, args);

    }


}


