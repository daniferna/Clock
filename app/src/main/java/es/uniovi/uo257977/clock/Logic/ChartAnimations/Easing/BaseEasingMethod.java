package es.uniovi.uo257977.clock.Logic.ChartAnimations.Easing;

import com.db.chart.animation.Animation;

public abstract class BaseEasingMethod extends Animation {

    public final static int ENTER = 0;
    public final static int UPDATE = 1;
    public final static int EXIT = 2;

    private static int mState;


    protected abstract float easeOut(float time);
    protected abstract float easeInOut(float time);
    protected abstract float easeIn(float time);


    /**
     * Method that gives the next interpolated value to be processed by
     * the {@link com.db.chart.animation.Animation} object.
     *
     * @param time - time normalized between 0 and 1
     * @return the next interpolation.
     */
    public float next(float time){

        if(mState == BaseEasingMethod.ENTER)
            return easeOut(time);
        else if(mState == BaseEasingMethod.UPDATE)
            return easeInOut(time);
        else if(mState == BaseEasingMethod.EXIT)
            return easeIn(time);
        return 1;
    }

    public int getState(){
        return mState;
    }


    /**
     * Whether interpolation should comply with ENTER, UPDATE, or EXIT animation.
     *
     * @param state
     */
    public void setState(int state){
        mState = state;
    }

}