package com.example.myapplication.faceScan;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.util.Log;

import com.example.myapplication.R;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.Landmark;

/**
 * Graphic instance for rendering face position, orientation, and landmarks within an associated
 * graphic overlay view.
 */
public class FaceGraphicOld extends GraphicOverlayOld.Graphic
{
    private Bitmap marker;

    private static final double SMILE_PROB = 0.3;

    private BitmapFactory.Options opt;
    private Resources resources;

    private int faceId;
    private PointF facePosition;

    private float faceWidth;
    private float faceHeight;
    private PointF faceCenter;

    private float isSmilingProbability = -1;
    private float eyeRightOpenProbability = -1;
    private float eyeLeftOpenProbability = -1;
    private float eulerZ;
    private float eulerY;

    private PointF leftEyePos = null;
    private PointF rightEyePos = null;
    private PointF noseBasePos = null;
    private PointF leftMouthCorner = null;
    private PointF rightMouthCorner = null;
    private PointF mouthBase = null;
    private PointF leftEar = null;
    private PointF rightEar = null;
    private PointF leftEarTip = null;
    private PointF rightEarTip = null;
    private PointF leftCheek = null;
    private PointF rightCheek = null;

    private volatile Face mFace;
    private ISmileDetector smileCallback;


    public FaceGraphicOld(GraphicOverlayOld overlay, ISmileDetector smile, Context context)
    {
        super(overlay);

        opt = new BitmapFactory.Options();
        opt.inScaled = false;
        resources = context.getResources();
        marker = BitmapFactory.decodeResource(resources, R.drawable.marker, opt);

        this.smileCallback = smile;
    }

    public void setId(int id)
    {
        faceId = id;
    }

    public float getSmilingProbability()
    {
        return isSmilingProbability;
    }

    public float getEyeRightOpenProbability()
    {
        return eyeRightOpenProbability;
    }

    public float getEyeLeftOpenProbability()
    {
        return eyeLeftOpenProbability;
    }

    /**
     * Updates the face instance from the detection of the most recent frame.  Invalidates the
     * relevant portions of the overlay to trigger a redraw.
     */
    public void updateFace(Face face)
    {
        mFace = face;
        postInvalidate();
    }

    public void goneFace()
    {
        mFace = null;
    }

    @Override
    public void draw(Canvas canvas)
    {
        Face face = mFace;

        if(face == null)
        {
            canvas.drawColor(0, PorterDuff.Mode.CLEAR);

            isSmilingProbability = -1;
            eyeRightOpenProbability= -1;
            eyeLeftOpenProbability = -1;

            return;
        }

        facePosition = new PointF(translateX(face.getPosition().x), translateY(face.getPosition().y));
        faceWidth = face.getWidth() * 4;
        faceHeight = face.getHeight() * 4;
        faceCenter = new PointF(translateX(face.getPosition().x + faceWidth/8), translateY(face.getPosition().y + faceHeight/8));
        isSmilingProbability = face.getIsSmilingProbability();
        eyeRightOpenProbability = face.getIsRightEyeOpenProbability();
        eyeLeftOpenProbability = face.getIsLeftEyeOpenProbability();
        eulerY = face.getEulerY();
        eulerZ = face.getEulerZ();

        //DO NOT SET TO NULL THE NON EXISTENT LANDMARKS. USE OLDER ONES INSTEAD.
        for(Landmark landmark : face.getLandmarks())
        {
            switch (landmark.getType())
            {
                case Landmark.LEFT_EYE:

                    leftEyePos = new PointF(translateX(landmark.getPosition().x), translateY(landmark.getPosition().y));
                    break;

                case Landmark.RIGHT_EYE:

                    rightEyePos = new PointF(translateX(landmark.getPosition().x), translateY(landmark.getPosition().y));
                    break;

                case Landmark.NOSE_BASE:

                    noseBasePos = new PointF(translateX(landmark.getPosition().x), translateY(landmark.getPosition().y));
                    break;

                case Landmark.LEFT_MOUTH:

                    leftMouthCorner = new PointF(translateX(landmark.getPosition().x), translateY(landmark.getPosition().y));
                    break;

                case Landmark.RIGHT_MOUTH:

                    rightMouthCorner = new PointF(translateX(landmark.getPosition().x), translateY(landmark.getPosition().y));
                    break;

                case Landmark.BOTTOM_MOUTH:

                    mouthBase = new PointF(translateX(landmark.getPosition().x), translateY(landmark.getPosition().y));
                    break;

                case Landmark.LEFT_EAR:

                    leftEar = new PointF(translateX(landmark.getPosition().x), translateY(landmark.getPosition().y));
                    break;

                case Landmark.RIGHT_EAR:

                    rightEar = new PointF(translateX(landmark.getPosition().x), translateY(landmark.getPosition().y));
                    break;

                case Landmark.LEFT_EAR_TIP:

                    leftEarTip = new PointF(translateX(landmark.getPosition().x), translateY(landmark.getPosition().y));
                    break;

                case Landmark.RIGHT_EAR_TIP:

                    rightEarTip = new PointF(translateX(landmark.getPosition().x), translateY(landmark.getPosition().y));
                    break;

                case Landmark.LEFT_CHEEK:

                    leftCheek = new PointF(translateX(landmark.getPosition().x), translateY(landmark.getPosition().y));
                    break;

                case Landmark.RIGHT_CHEEK:

                    rightCheek = new PointF(translateX(landmark.getPosition().x), translateY(landmark.getPosition().y));
                    break;
            }
        }

        Paint mPaint = new Paint();

        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(4);

        if(faceCenter != null)
            canvas.drawBitmap(marker, faceCenter.x, faceCenter.y, null);
        if(noseBasePos != null)
            canvas.drawBitmap(marker, noseBasePos.x, noseBasePos.y, null);
        if(leftEyePos != null)
            canvas.drawBitmap(marker, leftEyePos.x, leftEyePos.y, null);
        if(rightEyePos != null)
            canvas.drawBitmap(marker, rightEyePos.x, rightEyePos.y, null);
        if(mouthBase != null)
            canvas.drawBitmap(marker, mouthBase.x, mouthBase.y, null);
        if(leftMouthCorner != null)
            canvas.drawBitmap(marker, leftMouthCorner.x, leftMouthCorner.y, null);
        if(rightMouthCorner != null)
            canvas.drawBitmap(marker, rightMouthCorner.x, rightMouthCorner.y, null);
        if(leftEar != null)
            canvas.drawBitmap(marker, leftEar.x, leftEar.y, null);
        if(rightEar != null)
            canvas.drawBitmap(marker, rightEar.x, rightEar.y, null);
        if(leftEarTip != null)
            canvas.drawBitmap(marker, leftEarTip.x, leftEarTip.y, null);
        if(rightEarTip != null)
            canvas.drawBitmap(marker, rightEarTip.x, rightEarTip.y, null);
        if(leftCheek != null)
            canvas.drawBitmap(marker, leftCheek.x, leftCheek.y, null);
        if(rightCheek != null)
            canvas.drawBitmap(marker, rightCheek.x, rightCheek.y, null);

Log.i("isSmilingProbability",isSmilingProbability+"");
        if(isSmilingProbability >= SMILE_PROB)
        {
            smileCallback.smiled();
        }
    }


    public interface ISmileDetector
    {
        void smiled();
    }
}