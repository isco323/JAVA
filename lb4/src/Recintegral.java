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
    public double getMin() {
        return Min;
    }

    public double getMax() {
        return Max;
    }

    public double getStep() {
        return Step;
    }

    public double getResult() {
        return Result;
    }

    public void setMin(double min) {
        Min = min;
    }

    public void setMax(double max) {
        Max = max;
    }

    public void setStep(double step) {
        Step = step;
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
            setResult(getResult());
        }
    }
    public void setResult(double result) {

        result =  Trap(Min,Max,Step);
        Result = result;
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
        double result = 0;
        double num = 0;
        for(double i=a;i<=b-(h*2);i+=h)
        {num=i+h;
            if(i>b)
            {
                num=b;
            }
            result+=(InFunction(i)+InFunction(num))*(b-i)/2;
        }
        return result;
    }
    public static double InFunction(double x) //Подынтегральная функция
    {
        return ((Math.pow(Math.E, x))/x);
    }
    @Override
    public String toString() {
        return Min+","+Max+","+Step+","+Result;
    }
}
