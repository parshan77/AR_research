package com.example.ar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.BaseArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import java.util.function.Consumer;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ArFragment arFragment;
    private ModelRenderable modelRenderable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        setupModel();
        setupPlane();
    }

    private void setupPlane() {
        Log.i(TAG, "setupPlane: called");
        arFragment.setOnTapArPlaneListener((hitResult, plane, motionEvent) -> {
            Log.i(TAG, "onTapPlane: user tapped at a point, adding model there ...");
            Anchor anchor = hitResult.createAnchor();
            AnchorNode anchorNode = new AnchorNode(anchor);
            anchorNode.setParent(arFragment.getArSceneView().getScene());
            addModelToScene(anchorNode);
        });
    }

    private void addModelToScene(AnchorNode anchorNode) {
        TransformableNode transformableNode = new TransformableNode(arFragment.getTransformationSystem());
        transformableNode.setParent(anchorNode);
        transformableNode.setRenderable(this.modelRenderable);
        transformableNode.select();
    }


    private void setupModel() {
        Log.i(TAG, "setupModel");
        ModelRenderable.builder().setSource(this, R.raw.fox)
                .build().thenAccept(renderable -> modelRenderable = renderable).exceptionally(throwable -> {
            Log.e(TAG, "setupModel: model could not be loaded");
            Toast.makeText(getApplicationContext(), " Model could not be loaded", Toast.LENGTH_SHORT).show();
            return null;
        });

    }


}