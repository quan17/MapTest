package com.example.chand_000.maptest;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.w3c.dom.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;

//import com.google.android.gms.maps.


public class Home_Screen extends Activity implements LocationListener, GoogleMap.OnMarkerDragListener {

    GoogleMap mMap;
    LatLng[] StopsLoc=new LatLng[13];
    private Handler busHandler;
    Marker oldM = null;
    Marker[] oldMarkers = new Marker[2];
    int GapTime=6;
    int PickupIndex = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home__screen);

        mMap = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
        mMap.setOnMarkerDragListener(this);

        StopsLoc[0]=new LatLng(40.741211, -74.178982);
        StopsLoc[1]=new LatLng(40.740027, -74.179229);
        StopsLoc[2]=new LatLng(40.744663, -74.168774);
        StopsLoc[3]=new LatLng(40.745364, -74.164158);
        StopsLoc[4]=new LatLng(40.747433, -74.156424);
        StopsLoc[5]=new LatLng(40.755515, -74.155404);
        StopsLoc[6]=new LatLng(40.765869, -74.147607);
        StopsLoc[7]=new LatLng(40.770602, -74.145153);
        StopsLoc[8]=new LatLng(40.761265, -74.159693);
        StopsLoc[9]=new LatLng(40.753206, -74.163086);
        StopsLoc[10]=new LatLng(40.746197, -74.171663);
        StopsLoc[11]=new LatLng(40.741609, -74.173912);
        StopsLoc[12]=new LatLng(40.741211, -74.178982);
//        StopsLoc[13]=new LatLng(40.741211, -74.178982);// void point protect the boundary of StopsLoc

        LocationManager locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(provider);
        locationManager.requestLocationUpdates(provider, 20000, 0, this);
        drawRouteByStops();
        BusRunnable.run();
    }

    @Override
    public void onResume(){
        super.onResume();
//        GoogleMap mMap = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home__screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLocationChanged(Location location) {
        if(mMap!=null)
        {
//            drawMarker(location);
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

     /*
    Runnable Thread for drawing moving bus
    */

    private Runnable BusRunnable= new Runnable() {
        int i=0;

        public void run() {
            busHandler= new Handler();
            oldM = drawBus(i%StopsLoc.length,oldM);
            busHandler.postDelayed(this, GapTime*1000);

//            oldM.remove();
            i++;
            }
    };




/*
   * Draw Marker current location in Map
   */

    private void drawMarker(Location location){
        mMap.clear();
        //  convert the location object to a LatLng object that can be used by the map API
        LatLng currentPosition = new LatLng(location.getLatitude(),
                location.getLongitude());

        // zoom to the current location
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 16));

        // add a marker to the map indicating our current position
        mMap.addMarker(new MarkerOptions().position(currentPosition).snippet("Lat:" + location.getLatitude() + "Lng:" + location.getLongitude()));
     }


      /*



    private void drawRoute(){
//
        PolylineOptions polylineoption=new PolylineOptions();
        LinkedList<LatLng> RoutePoints = RouteLoad();
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(40.73978,-74.17521),15));
        polylineoption.addAll(RoutePoints);
        polylineoption.width(10).color(Color.BLUE);
//        polylineoption.add(new LatLng(40.7443026, -74.179543), new LatLng(40.7442683, -74.1795127)).width(5).color(Color.YELLOW);
//        Polyline line = mMap.addPolyline(new PolylineOptions() .add(new LatLng(40.7443026,-74.179543), new LatLng(40.7442993,-74.1795329)).width(5).color(Color.YELLOW));
        mMap.addPolyline(polylineoption);
        MarkerOptions homemarker = new MarkerOptions().position(new LatLng( 40.74867,-74.15867));
        homemarker.title("HOME");
        mMap.addMarker(homemarker);
        mMap.addMarker(new MarkerOptions().position(new LatLng(40.74486,-74.16764)).title("BRIDGE"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(40.745650, -74.162218)).title("RIVER PARK"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(40.74486,-74.16764)).title("PARK"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(40.7443026,-74.179543)).title("SCHOOL"));
    }
*/


    /*
    * Draw Route in Map, add marker as hotstops.
    */
    public void drawRouteByStops(){
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(40.743752, -74.168451), 15));




        mMap.addMarker(new MarkerOptions().position(StopsLoc[0]).title("NJIT"));
        mMap.addMarker(new MarkerOptions().position(StopsLoc[11]).title("RUTGERS"));
        mMap.addMarker(new MarkerOptions().position(StopsLoc[3]).title("RIVER PARK"));
        mMap.addMarker(new MarkerOptions().position(StopsLoc[4]).title("Frank E St"));
        mMap.addMarker(new MarkerOptions().position(StopsLoc[6]).title("Midland Ave"));
        mMap.addMarker(new MarkerOptions().position(StopsLoc[8]).title("K-Mart"));

        PolylineOptions polylineoption = new PolylineOptions();
//        LinkedList<LatLng> RoutePoints = new LinkedList<LatLng>();
//        RoutePoints.addAll(StopsLoc);

//        polylineoption.add(StopsLoc);
//        polylineoption.width(10).color(Color.BLUE);
//        mMap.addPolyline(polylineoption);


        GetdTask getDocTask= new GetdTask();
        getDocTask.execute(StopsLoc);


//        mMap.addPolyline(polylineoption);

    }


    /*
   * Draw Bus in Map, update remove old Bus Marker
   */
    private Marker drawBus(int BusLocIndex, Marker oldBus){
        if (oldBus != null) {
            oldBus.remove();
        }

        Bitmap BitBusIcon = BitmapFactory.decodeResource(this.getResources(),R.drawable.busicon);
        //resize value is reciprocal  <15
        int resize= 12;
        Bitmap ResizeBusIcon = Bitmap.createScaledBitmap(BitBusIcon,BitBusIcon.getWidth()/resize,BitBusIcon.getHeight()/resize,false);


        MarkerOptions Busmarker = new MarkerOptions().position(StopsLoc[BusLocIndex]);
        Busmarker.title("Bus").icon(BitmapDescriptorFactory.fromBitmap(ResizeBusIcon));

        oldBus = mMap.addMarker(Busmarker);


        oldMarkers = drawPeople(BusLocIndex,oldMarkers[0],oldMarkers[1]);



        return oldBus;

    }

    /*
    Read route file in assets folder, store in list<LatLng>
    */
    private LinkedList<LatLng> RouteLoad(){
        LinkedList<LatLng> RoutePoints= new LinkedList<LatLng>();
        String line="";
        String[] linesplice;
        try
        {
            InputStream instream=getAssets().open("Route.txt");
            BufferedReader in=new BufferedReader(new InputStreamReader(instream));
            System.out.println("RouteFind");

            line=in.readLine();
            while (line!=null)
                {
                    linesplice = line.split(",");
                    if(linesplice.length==2)
                        RoutePoints.add(new LatLng(Double.parseDouble(linesplice[0]),Double.parseDouble(linesplice[1])));
                    line=in.readLine();
                }
            in.close();

            } catch (IOException e1) {
            e1.printStackTrace();
        }
        return RoutePoints;
    }

    /*
   * Draw People and pickup point in Map, remove old one
   */

    public Marker[] drawPeople(int BusLocIndex, Marker oldPassenger, Marker oldPickup){
        MarkerOptions userMarker = new MarkerOptions().position(new LatLng( 40.743752, -74.168451));
        MarkerOptions PickupMarker = new MarkerOptions().position(StopsLoc[PickupIndex]).title("PickMeUp");
        if (oldPassenger != null) {
//            oldPassenger.setSnippet("\n"+oldPassenger.getPosition().longitude+","+oldPassenger.getPosition().latitude);
//            oldPassenger.showInfoWindow();
//            userMarker.position(oldPassenger.getPosition());
            oldPassenger.remove();
        }
        if (oldPickup != null) {
//            PickupMarker.position(oldPassenger.getPosition());
            oldPickup.remove();
        }


//        MarkerOptions userMarker = new MarkerOptions().position(new LatLng( 40.743752, -74.168451));
        userMarker.title("User");

        Bitmap BitPeopleIcon = BitmapFactory.decodeResource(this.getResources(),R.drawable.walkericon);
        //resize value is reciprocal  <15
        int resize= 12;
        Bitmap ResizePeopleIcon = Bitmap.createScaledBitmap(BitPeopleIcon,BitPeopleIcon.getWidth()/resize,BitPeopleIcon.getHeight()/resize,false);
        userMarker.icon(BitmapDescriptorFactory.fromBitmap(ResizePeopleIcon));

        Bitmap BitPickupIcon = BitmapFactory.decodeResource(this.getResources(),R.drawable.pickupicon);
        //resize value is reciprocal  <15
        int resize2= 8;
        Bitmap ResizePickupIcon = Bitmap.createScaledBitmap(BitPickupIcon, BitPickupIcon.getWidth() / resize2, BitPickupIcon.getHeight() / resize2, false);
        PickupMarker.icon(BitmapDescriptorFactory.fromBitmap(ResizePickupIcon));





        int BusRemainTime=0;

        if(BusLocIndex>PickupIndex)
            BusRemainTime= GapTime*(PickupIndex+StopsLoc.length-1-BusLocIndex);
        else
            BusRemainTime= GapTime*(PickupIndex-BusLocIndex);


//        userMarker.snippet("Next bus: "+BusRemainTime+" s");
        oldPassenger = mMap.addMarker(userMarker);
//        oldPassenger.setDraggable(true);
       oldPassenger.setSnippet("\n"+oldPassenger.getPosition().longitude+","+oldPassenger.getPosition().latitude);
//
        oldPassenger.showInfoWindow();



        PickupMarker.snippet("Next bus: "+BusRemainTime+" s");
        oldPickup=mMap.addMarker(PickupMarker);
        oldPickup.setDraggable(true);
//        oldPassenger.setSnippet("\n"+oldPassenger.getPosition().longitude+","+oldPassenger.getPosition().latitude);

        oldPickup.showInfoWindow();

        Marker[] MarkerResult= new Marker[2];
        MarkerResult[0]=oldPassenger;
        MarkerResult[1]=oldPickup;
        return MarkerResult;

    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }
    /*
       *  relocate the user , change pickup point also
       */
    @Override
    public void onMarkerDragEnd(Marker marker) {

        marker.setSnippet("Next Bus : Recalculating..");
        marker.showInfoWindow();
        int stopsIndex= markerBelongtoStops(marker);
        marker.setPosition(StopsLoc[stopsIndex]);
        PickupIndex = stopsIndex;
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(StopsLoc[stopsIndex], 16));


    }

    /*
   * find nearest point for marker
   */

    public int markerBelongtoStops(Marker marker){
        double dis[]= new double[StopsLoc.length];
        double minDis=10000.0;
        int minDisIndex=0;
        for (int i=0;i<StopsLoc.length;i++)
        {
            dis[i]=gps2m(marker.getPosition().latitude,marker.getPosition().longitude,StopsLoc[i].latitude,StopsLoc[i].longitude);
            if(dis[i]<=minDis)
            {
                minDis = dis[i];
                minDisIndex=i;
            }
        }
        return minDisIndex;

    }


    /*
   * Async task get respoding from google map return the routes
   */
    public class GetdTask extends AsyncTask <LatLng[],Void,PolylineOptions>{


        @Override
        protected PolylineOptions doInBackground(LatLng[]... params) {

                GetDirection getd = new GetDirection();
                PolylineOptions polylineoption = new PolylineOptions();
                polylineoption.width(10).color(Color.RED);
                for(int ii=0;ii<params[0].length-1;ii++) {
                    Document doc = getd.getDocument(params[0][ii], params[0][(ii+1)%params[0].length], getd.MODE_DRIVING);
                    ArrayList<LatLng> directionPoint = getd.getDirection(doc);
                    for (int i = 0; i < directionPoint.size(); i++) {
                        polylineoption.add(directionPoint.get(i));
//                        System.out.println(directionPoint.get(i).latitude + "," + directionPoint.get(i).longitude);

                    }
//                    try {
//                        if(ii%10>0)
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                }

                return polylineoption;

        }

        @Override
        protected void onPostExecute(PolylineOptions result) {
            // TODO Auto-generated method stub

            super.onPostExecute(result);
            mMap.addPolyline(result);
        }

    }

    /*
   *  Gps Distance calculation
   */

        private final double EARTH_RADIUS = 6378137.0;
        private double gps2m(double lat_a, double lng_a, double lat_b, double lng_b) {

           double radLat1 = (lat_a * Math.PI / 180.0);
           double radLat2 = (lat_b * Math.PI / 180.0);
           double a = radLat1 - radLat2;
           double b = (lng_a - lng_b) * Math.PI / 180.0;
           double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                  + Math.cos(radLat1) * Math.cos(radLat2)
                  * Math.pow(Math.sin(b / 2), 2)));
           s = s * EARTH_RADIUS;
           s = Math.round(s * 10000) / 10000;
           return s;
        }


}
