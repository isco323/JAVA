public class Recintegral {
    private double Min;
    private double Max;
    private double Step;
    private double Result;

    public Recintegral(double min, double max, double step, double result) {
        Min = min;
        Max = max;
        Step = step;
        Result = result;
    }
    public Recintegral() {
        Min = 0;
        Max = 0;
        Step = 0;
        Result = 0;
    }
    public Recintegral(double[] temp) {

        Min = temp[0];
        Max = temp[1];
        Step = temp[2];
        Result = Trap(temp[0],temp[1],temp[2]);
    }
    public void setAllField(double a, double b,double step)
    {
        Min = a;
        Max = b;
        Step = step;
        if(Min>Max)
        {
            setResultNull();
        }else {
            setResult();
        }
    }
    public void setResult() {

        Result =  Trap(Min,Max,Step);
    }
    public void setResultNull() {

        Result =  0;
    }

    public Object[] addMod() {
        Object[] temp=new Object[4];
        temp[0]=Min;
        temp[1]=Max;
        temp[2]=Step;
        temp[3]=Result;
        return temp;
    }
    public double Trap(double a,double b, double h){
        double area = 0;
        if (h == 0) return area;

        for(int i = 0; i < (b-a)/h; i++){
            area +=InFunction(a + i*h);
        }
        area += (InFunction(a)+InFunction(b))/2;
        area *= h;
        return area;
    }
    public static double InFunction(double x) //Подынтегральная функция
    {
        return ((Math.pow(Math.E, x))/x);
    }
    @Override
    public String toString() {
        return "DataNumber{" +
                "Min=" + Min +
                ", Max=" + Max +
                ", Step=" + Step +
                ", Result=" + Result +
                '}';
    }
}
