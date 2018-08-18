/*
 * Copyright 2018 Google LLC. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.ar.sceneform.samples.hellosceneform;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.samples.hellosceneform.model.Battlefield;
import com.google.ar.sceneform.ux.ArFragment;

/**
 * This is an example activity that uses the Sceneform UX package to make common AR tasks easier.
 */
public class HelloSceneformActivity extends AppCompatActivity {
    private static final String TAG = HelloSceneformActivity.class.getSimpleName();
    private static final double MIN_OPENGL_VERSION = 3.0;

    private ArFragment arFragment;
    private ModelRenderable blank, nothing, yep;
    boolean existCube = false;
    Node[][] nodeArray;
    Switch addSh;

    @Override
    @SuppressWarnings({"AndroidApiChecker", "FutureReturnValueIgnored"})
    // CompletableFuture requires api level 24
    // FutureReturnValueIgnored is not valid
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!checkIsSupportedDeviceOrFinish(this)) {
            return;
        }

        addSh = findViewById(R.id.switch1);

        nodeArray = new Node[10][10];
        Node[][] enemyNode = new Node[9][9];

        setContentView(R.layout.activity_ux);
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);

        // When you build a Renderable, Sceneform loads its resources in the background while returning
        // a CompletableFuture. Call thenAccept(), handle(), or check isDone() before calling get().
        ModelRenderable.builder()
                .setSource(this, R.raw.blank2)
                .build()
                .thenAccept(renderable -> blank = renderable)
                .exceptionally(
                        throwable -> {
                            Toast toast =
                                    Toast.makeText(this, "Unable to load andy renderable", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            return null;
                        });
        ModelRenderable.builder()
                .setSource(this, R.raw.nothing)
                .build()
                .thenAccept(renderable -> nothing = renderable)
                .exceptionally(
                        throwable -> {
                            Toast toast =
                                    Toast.makeText(this, "Unable to load andy renderable", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            return null;
                        });
        ModelRenderable.builder()
                .setSource(this, R.raw.tugboat)
                .build()
                .thenAccept(renderable -> yep = renderable)
                .exceptionally(
                        throwable -> {
                            Toast toast =
                                    Toast.makeText(this, "Unable to load andy renderable", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            return null;
                        });
        if (!existCube) {
            arFragment.setOnTapArPlaneListener(
                    (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
                        Battlefield mine = new Battlefield();
                        //Battlefield.OneShip oneShip = new Battlefield.OneShip(new Point(1, 1), new Point(1, 1));


                        //Toast.makeText(HelloSceneformActivity.this, "!!!!!!", Toast.LENGTH_SHORT).show();
                        Anchor anchor = hitResult.createAnchor();
                        AnchorNode anchorNode = new AnchorNode(anchor);
                        anchorNode.setParent(arFragment.getArSceneView().getScene());

                        Node baseNode = new Node();
                        anchorNode.addChild(baseNode);
                        baseNode.setWorldScale(new Vector3(.02f, .025f,.02f));
//                baseNode.setLocalPosition(new Vector3(0.2f, 0.001f, .2f));

                        float count = 9;
                        float offset = 1.7f;
                        //   for(int y = 0; y<2; y++) {
                        //float step = 0.01f / count;
                        for (int i = 0; i < count; i++) {
                            for (int j = 0; j < count; j++) {
                                //   float offset = (step - 0.28f);
                                addObject(baseNode, offset * j,
                                        offset * i, mine, j, i);

                            }
                        }
                        //            }
                        existCube = true;

                    });
        }
    }

    public void addObject(Node baseNode, float xOffset, float zOffset, Battlefield mine, int xpos, int ypos) {
        Node node = new Node();
        node.setParent(baseNode);
        node.setRenderable(blank);
        node.setLocalPosition(new Vector3(xOffset, 0.0f, zOffset));
        node.setOnTapListener((hitTestResult, motionEvent) -> {
            //  if (addSh.isActivated()) {
            // mine.addShip();
            node.setRenderable(yep);
            //        } else {
//                        if (enemy.fire(new Point(xpos, ypos)))
//                            node.setRenderable(yep);
//                        else
//                            node.setRenderable(nothing);
     //   }
    }
        );

    nodeArray[xpos][ypos]=node;
}


    /**
     * Returns false and displays an error message if Sceneform can not run, true if Sceneform can run
     * on this device.
     * <p>
     * <p>Sceneform requires Android N on the device as well as OpenGL 3.0 capabilities.
     * <p>
     * <p>Finishes the activity if Sceneform can not run
     */
    public static boolean checkIsSupportedDeviceOrFinish(final Activity activity) {
        if (Build.VERSION.SDK_INT < VERSION_CODES.N) {
            Log.e(TAG, "Sceneform requires Android N or later");
            Toast.makeText(activity, "Sceneform requires Android N or later", Toast.LENGTH_LONG).show();
            activity.finish();
            return false;
        }
        String openGlVersionString =
                ((ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE))
                        .getDeviceConfigurationInfo()
                        .getGlEsVersion();
        if (Double.parseDouble(openGlVersionString) < MIN_OPENGL_VERSION) {
            Log.e(TAG, "Sceneform requires OpenGL ES 3.0 later");
            Toast.makeText(activity, "Sceneform requires OpenGL ES 3.0 or later", Toast.LENGTH_LONG)
                    .show();
            activity.finish();
            return false;
        }
        return true;
    }
}
