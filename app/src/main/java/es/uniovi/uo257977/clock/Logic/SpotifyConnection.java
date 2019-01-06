package es.uniovi.uo257977.clock.Logic;

import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.types.Track;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class SpotifyConnection extends AppCompatActivity {


    //credenciales spotify
    private static final String CLIENT_ID = "f354fa8aa2dc4a549b0c211d355e6486";
    private static final String REDIRECT_URI = "testschema://callback";
    private SpotifyAppRemote mSpotifyAppRemote;
    private static final String OAuthToken ="BQCGacI2HG_5I4qD-ghVrJc397p7eD1stfAk96ACikOi8xm__w_Tkd85EbaMrQHUORORxlJpvfmY3jRtNfU2XdFsOCnZAU5QrhDrFRyyMzKt7So_eNcRIGvxDXwGlDBxMGPAiNqR92xVkg";
    ArrayList<String> SpotifyPlaylist;




    public SpotifyConnection(){

        crearPlayList();
    }


    @Override
    protected void onStart() {
        super.onStart();

        connect();

    }


    @Override
    protected void onStop() {
        super.onStop();
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private void crearPlayList() {

        SpotifyPlaylist= new ArrayList<String>();
        SpotifyPlaylist.add("spotify:playlist:37i9dQZEVXbNFJfN1Vw8d9");
        SpotifyPlaylist.add("spotify:playlist:37i9dQZF1DWZoF06RIo9el");
        SpotifyPlaylist.add("spotify:playlist:37i9dQZF1DWUa8ZRTfalHk");
        SpotifyPlaylist.add("spotify:playlist:37i9dQZF1DX8f6LHxMjnzD");
        SpotifyPlaylist.add("spotify:playlist:37i9dQZEVXbLiRSasKsNU9");
        SpotifyPlaylist.add("spotify:playlist:37i9dQZEVXbMDoHDwVN2tF");
        SpotifyPlaylist.add("spotify:playlist:37i9dQZF1DX2sUQwD7tbmL");

    }
    private void connect(){
        ConnectionParams connectionParams = new ConnectionParams.Builder(CLIENT_ID)
                .setRedirectUri(REDIRECT_URI)
                .showAuthView(true)
                .build();
        SpotifyAppRemote.connect(this, connectionParams,
                new Connector.ConnectionListener() {

                    @Override
                    public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                        mSpotifyAppRemote = spotifyAppRemote;
                        Log.d("MainActivity2", "Connected!!");
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.e("MainActivity2.0", throwable.getMessage(), throwable);

                    }
                });

    }


    public void activateSpotifyAlarm() {

        // Play a playlist
        int playListForToday= (int)(Math.random() *8);
        mSpotifyAppRemote.getPlayerApi().play(SpotifyPlaylist.get(playListForToday));
        mSpotifyAppRemote.getPlayerApi()
                .subscribeToPlayerState()
                .setEventCallback(playerState -> {
                    final Track track = playerState.track;
                    if (track != null) {
                        Log.d("MainActivity", track.name + " by " + track.artist.name);
                    }
                });
    }


    private void getClientConection(){
        String url = " https://accounts.spotify.com/authorize?client_id="+CLIENT_ID+"&redirect_uri="+REDIRECT_URI+"callback&scope=user-read-private%20user-read-email&response_type=token&state=123";

        RequestQueue queue = Volley.newRequestQueue(this);
        Log.d("vamoh a vehjeje","Response is: ");

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        // Display the first 500 characters of the response string.

                        Log.d("vamoh a veh222","Response is:"+ response);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // mTextView.setText("That didn't work!");
                Log.d("vamoh a veh3", error+ " "+ error.getCause());
            }
        }
        );

        queue.add(stringRequest);


    }
/*
    private void getPlayList(){

        String url = "https://api.spotify.com/v1/playlists/59ZbFPES4DQwEjBpWHzrtC";



        RequestQueue queue = Volley.newRequestQueue(this);
        Log.d("vamoh a veh","Response is: ");

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    // Display the first 500 characters of the response string.

                    Log.d("vamoh a veh222","Response is: 2 ");

                }
            }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // mTextView.setText("That didn't work!");
                Log.d("vamoh a veh3", error+ " "+ error.getCause());
            }
            }
             );

        queue.add(stringRequest);
    }*/






}
