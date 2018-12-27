/*
 * AUTHOR: Lowell Stadelman
 */

package controller;

import javafx.animation.*;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/*******************************************************************************
 * CLASS DESCRIPTION: FMTransition class provides the necessary methods and 
 * variables for transitions in the Calculator program.
 ******************************************************************************/

/*** IMPORTS ***/


public abstract class FMTransition
{
    
// *** Transition objects ***

    private static TranslateTransition qRight;
    public static TranslateTransition aRight;
    protected static TranslateTransition rpCenterLeft;
    protected static TranslateTransition ansTop;
    protected static TranslateTransition qLeft;
    protected static TranslateTransition aLeft;
    protected static TranslateTransition sBottom;
    
    protected static SequentialTransition ansWaitTop; // moves answer fm top
    protected static SequentialTransition goodNod;
    protected static SequentialTransition badNod;
    
    protected static FadeTransition lbFadeIn;
    public static FadeTransition rbFadeIn;
    protected static FadeTransition topFadeIn;
    protected static FadeTransition createFadeIn;
    //protected static FadeTransition ansButtonFadeIn;


    public static void setQRight(TranslateTransition rightQTransition)
    {

        System.out.println("\n\t *!*!* called setQRight() *!*!*");

        qRight = rightQTransition;
    }

    public static TranslateTransition getQRight()
    {

        System.out.println("\n\t *!*!* called getQRight *!*!*");

        return qRight;
    }

    public static void setQLeft(TranslateTransition leftQTransition)
    {
        qLeft = leftQTransition;
    }

    public static TranslateTransition getQLeft()
    {
        return qLeft;
    }

    public static void setAWaitTop(SequentialTransition aTransition)
    {
        ansWaitTop = aTransition;
    }

    public static void setTransitionBtm(TranslateTransition bottomTransition) { sBottom = bottomTransition; }

    public static SequentialTransition getAWaitTop()
    {
        return ansWaitTop;
    }

    public static TranslateTransition getSBottom() {
        return sBottom;
    }
    
    
    /**
     * Question transition from right. 
     * @param node
     */
    public static TranslateTransition transitionFmRight(Node node)
    {
        // @test
        System.out.println("transitionFmRight called: FMTransition line 60");

        // Transition the textframe
    	TranslateTransition trans = new TranslateTransition(Duration.millis(500), node);
    	trans.setFromX(300f);
    	trans.setToX(0);
    	trans.setCycleCount(1);
    	trans.setAutoReverse(false);
    	
    	return trans;
    }
  
     
    /**
     * Transition from top. 
     * @param node
     */
    public static TranslateTransition transitionFmTop(Node node, int duration, int fmHt)
    {
        // @test
        System.out.println("transitionFmTop called: FMTransition line 80");

    	TranslateTransition trans = new TranslateTransition(Duration.millis(duration), node);
    	trans.setFromY(-fmHt);
    	trans.setToY(0);
    	trans.setCycleCount(1);
    	trans.setAutoReverse(false);     
    	
    	return trans;    
    }
    
    /**
     * Transition from bottom. 
     * @param node
     */
    public static TranslateTransition transitionFmBottom(Node node)
    {
        // @test
        System.out.println("transitionFmBottom called: FMTransition line 95");

    	TranslateTransition trans = new TranslateTransition(Duration.millis(500), node);
    	trans.setFromY(-100f);
    	trans.setToY(0);
    	trans.setCycleCount(1);
    	trans.setAutoReverse(false);     
    	
    	return trans;    
    }
    
    /**
     * Transition from left
     * @param node
     */
    public static TranslateTransition transitionFmLeft(Node node)
    {
        // @test
        System.out.println("transitionFmLeft called: FMTransition line 116");

        // Transition the textframe
        TranslateTransition trans = new TranslateTransition(Duration.millis(500), node);
        trans.setFromX(-300f);
        trans.setToX(0);
        trans.setCycleCount(1);
        trans.setAutoReverse(false);
        
        return trans;
    }
    
    /**
     * Sequential transition from QPane from left,then AnsPane from top
     * @param node: The nodeObject that will transition
     * @return SequentialTransition
     */
 /*   public static SequentialTransition newQRightTop(Node node)
    {
        // @test
        System.out.println("newQRightTop called: FMTransition line 133");

    	TranslateTransition transLeft = new TranslateTransition();
    	transLeft = transitionFmLeft(node);
    	TranslateTransition transTop = new TranslateTransition();
    	transTop = transitionFmTop(node);
    	
    	return new SequentialTransition(node, transLeft, transTop);
    }
 */
    /**
     * Nod LR (left right) fade animation used by the select button
     * when the answer is incorrect. Object in argument fades slightly
     * then moves in a left right animation, then fades out.
     * @param node
     * @return SequentialTransition
     */
    public static SequentialTransition nodLRFade(Node node)
    {
        // @test
        System.out.println("nodLRFade called: FMTransition line 156");

    	FadeTransition aTrans = new FadeTransition(Duration.millis(100));
    	aTrans.setFromValue(1.0);
    	aTrans.setToValue(.6);
    	aTrans.setCycleCount(1);
    	aTrans.setAutoReverse(true);
    	
    	TranslateTransition trans = new TranslateTransition(Duration.millis(400));
        trans.setFromX(0f);
        trans.setToX(-20f);
        trans.setCycleCount(4);
        trans.setAutoReverse(true);
        
        FadeTransition fTrans = new FadeTransition(Duration.millis(400));
    	fTrans.setFromValue(.7);
    	fTrans.setToValue(.0);
    	fTrans.setCycleCount(1);
    	fTrans.setAutoReverse(true);
        
        
        return new SequentialTransition(node, aTrans, trans, fTrans);
    }
    
    /*public static SequentialTransition nodUpDownFade(Node button)
    {
    	//FillTransition ft = colorTrans(button, 200.0);
    	return nodUpDownFade(button);
    }*/
    /**
     * Node up down used by the select button when the 
     * answer is correct. Object in argument fades slightly,
     * then moves in an up down animation, then fades out
     * @param button
     * @return SequentialTransition
     */
    public static SequentialTransition nodUpDownFade(Node button, int millis)
    {
        // @test
        System.out.println("nodUpDownFade called: FMTransition line 195");

    	//int millis = 300;
    	FadeTransition fade = new FadeTransition(Duration.millis(75));
    	fade.setFromValue(1.0);
    	fade.setToValue(0);
    	fade.setCycleCount(1);
    	fade.setAutoReverse(false);
    	
    	TranslateTransition btrans = new TranslateTransition(Duration.millis(millis));
        btrans.setFromY(0f);
        btrans.setToY(-8f);
        btrans.setCycleCount(1);
        btrans.setAutoReverse(false);
        
        TranslateTransition ctrans = new TranslateTransition(Duration.millis(millis));
        ctrans.setFromY(-8f);
        ctrans.setToY(8f);
        ctrans.setCycleCount(1);
        ctrans.setAutoReverse(false);
        
        TranslateTransition dtrans = new TranslateTransition(Duration.millis(millis));
        dtrans.setFromY(8f);
        dtrans.setToY(0f);
        dtrans.setCycleCount(1);
        dtrans.setAutoReverse(false);
        
        return new SequentialTransition(button, btrans, ctrans, dtrans, fade);
    }
    
    /**
     * lbFadeIn answer Button fade in. Button fades from 1% to
     * 100%. Used on transition from left. 
     * @param node, the node object
     * @param cycle count
     * @param autoReverse true or not
     */
    public static FadeTransition fadeIn(Node node, int cycle, double duration, 
    		boolean autoReverse) 
    {
        // @test
        System.out.println("fadeIn called: FMTransition line 232");

    	FadeTransition fTrans = new FadeTransition(Duration.millis(duration), node);
    	fTrans.setFromValue(.1);
    	fTrans.setToValue(1.0);
    	fTrans.setCycleCount(cycle);
    	fTrans.setAutoReverse(autoReverse);
    	
    	return fTrans;    	
    }
    
    /**
     * lbFadeIn Left Button fade in. Button fades from 1% to
     * 100%. Used on transition from left. 
     * @param node, the node object
     * @param cycle count
     * @param autoReverse true or not
     */
    public static FadeTransition fadeTrans(Node node, Double from, Double to, int cycle, double duration,
    		boolean autoReverse)  
    {
        // @test
        System.out.println("newFade called: FMTransition line 254");

    	FadeTransition fTrans = new FadeTransition(Duration.millis(duration), node);
    	fTrans.setFromValue(from);
    	fTrans.setToValue(to);
    	fTrans.setCycleCount(cycle);
    	fTrans.setAutoReverse(autoReverse);
    	
    	return fTrans;    	
    }    
    
    /**
     * Wait Transition from top. 
     * @param node
     */
    public static SequentialTransition waitTransFmTop(Node node, int fmHt, int duration, int pause)
    {
        // @test
        System.out.println("waitTransFmTop called: FMTransition line 276");

    	FadeTransition fadeTrans;// = new FadeTransition();
    	fadeTrans = fadeTrans(node, 0.0, 1.0, 1, pause, false);
    	TranslateTransition trans;// = new TranslateTransition();
        trans = transitionFmTop(node, duration, fmHt);
        SequentialTransition seqTrans = new SequentialTransition(
        		new PauseTransition(Duration.millis(pause)),
        		fadeTrans,
        		trans
        ); 
        return seqTrans;        
    }

    
    public static FillTransition colorTrans(Node node, int millis)
    {
        //@test
        System.out.println("colorTrans called: FMTransition line 293");

    	FillTransition ft = new FillTransition();
    //	//ft.
    	ft.setDuration(Duration.millis(millis));
    	Color green = Color.rgb(0, 255, 53);
    	Color white = Color.rgb(255, 255, 255);
    	ft.setFromValue(white);
    	ft.setToValue(green);
        ft.setAutoReverse(true);
    	return ft;
    }

}

