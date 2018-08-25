package com.google.ar.sceneform.samples.hellosceneform.model;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.AdvertisingOptions;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.ConnectionResolution;
import com.google.android.gms.nearby.connection.ConnectionsStatusCodes;
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo;
import com.google.android.gms.nearby.connection.DiscoveryOptions;
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;
import com.google.android.gms.nearby.connection.Strategy;
import com.google.gson.Gson;

public class ConnectionChannel {
    public static final String TAG = "Channel";

    private final String serverId;
    private final String endpoint;
    private final Context context;

    private final EndpointDiscoveryCallback endpointDiscoveryCallback = new EndpointDiscoveryCallback() {
        @Override
        public void onEndpointFound(String s, DiscoveredEndpointInfo discoveredEndpointInfo) {
            Log.d(TAG, "onEndpointFound() called with: s = [" + s + "], discoveredEndpointInfo = [" + discoveredEndpointInfo + "]");
        }

        @Override
        public void onEndpointLost(String s) {
            Log.d(TAG, "onEndpointLost() called with: s = [" + s + "]");
        }
    };

    private final ConnectionLifecycleCallback connectionLifecycleCallback = new ConnectionLifecycleCallback() {
        @Override
        public void onConnectionInitiated(String endpointId, ConnectionInfo connectionInfo) {
            // Automatically accept the connection on both sides.
            Nearby.getConnectionsClient(context).acceptConnection(endpointId, new PayloadCallback() {
                @Override
                public void onPayloadReceived(String s, Payload payload) {
                    Log.d(TAG, "onPayloadReceived() called with: s = [" + s + "], payload = [" + payload + "]");
                    Message message = new Gson().fromJson(new String(payload.asBytes()), Message.class);

                    Toast.makeText(context, message.toString(), Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onPayloadTransferUpdate(String s, PayloadTransferUpdate payloadTransferUpdate) {
                    Log.d(TAG, "onPayloadTransferUpdate() called with: s = [" + s + "], payloadTransferUpdate = [" + payloadTransferUpdate + "]");
                }
            });

            connect(endpointId);
        }

        @Override
        public void onConnectionResult(String endpointId, ConnectionResolution result) {
            switch (result.getStatus().getStatusCode()) {
                case ConnectionsStatusCodes.STATUS_OK:
                    // We're connected! Can now start sending and receiving data.
                    break;
                case ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED:
                    // The connection was rejected by one or both sides.
                    break;
                default:
                    // The connection was broken before it was accepted.
                    break;
            }
            Log.d(TAG, "onConnectionResult() called with: endpointId = [" + endpointId + "], result = [" + result + "]");
        }

        @Override
        public void onDisconnected(String endpointId) {
            // We've been disconnected from this endpoint. No more data can be
            // sent or received.

            Log.d(TAG, "onDisconnected() called with: endpointId = [" + endpointId + "]");

        }

    };

    public ConnectionChannel(Context context, String serviceId, String endpoint){
        this.context = context;
        this.serverId = serviceId;
        this.endpoint = endpoint;
    }

    public void advertise(){
        Nearby.getConnectionsClient(context)
                .startAdvertising(endpoint, serverId,
                        connectionLifecycleCallback,
                        new AdvertisingOptions(Strategy.P2P_CLUSTER));
    }

    public void discover(){
        Nearby.getConnectionsClient(context)
                .startDiscovery(
                        serverId, endpointDiscoveryCallback,
                        new DiscoveryOptions(Strategy.P2P_CLUSTER));
    }

    public void connect(String advertiserEndpointId){
        Nearby.getConnectionsClient(context)
                .requestConnection(
                        endpoint,
                        advertiserEndpointId,
                        connectionLifecycleCallback);
    }

    public void sendMessage(String text, String endpointId){
        Nearby.getConnectionsClient(context).sendPayload(endpointId,  Payload.fromBytes(text.getBytes()));
    }

}
