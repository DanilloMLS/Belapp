package br.com.belapp.belapp.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


import br.com.belapp.belapp.R;

import br.com.belapp.belapp.model.Servico;
import br.com.belapp.belapp.servicos.MyServiceLocation;

import static br.com.belapp.belapp.servicos.PermissaoKt.validaPermissoes;


public class InicialActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //logalização
    MyServiceLocation localizacao;
    private ProgressDialog mProgressDialog;
    private ArrayList<String> ids;
    private ArrayList<String> idcateg;
    private String[] permissoesNecessarias = new String[]{
            Manifest.permission.GET_ACCOUNTS,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.CHANGE_NETWORK_STATE
    };
    private boolean mPermissoesConcedidas;
    private static final String TAG = "Debug";

    ImageButton btnBarba, btnCabelo, btnDepilacao, btnOlho, btnSobrancelha, btnUnha;

    private void openCategoriaIntent(@NonNull Intent intent, @NonNull String categoria) {
        intent.putExtra("mCategoria", categoria);
        intent.putExtra("mLatitude", localizacao.getLatitude());
        intent.putExtra("mLongitude", localizacao.getLongitude());
        intent.putExtra("mIds", ids);
        intent.putExtra("mIdcateg", idcateg);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPermissoesConcedidas = validaPermissoes(1,this,permissoesNecessarias);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_activity_main);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        localizar();

        btnBarba = findViewById(R.id.ibBarba);
        btnCabelo = findViewById(R.id.ibCabelo);
        btnDepilacao = findViewById(R.id.ibDepilacao);
        btnOlho = findViewById(R.id.ibOlho);
        btnSobrancelha = findViewById(R.id.ibSobrancelha);
        btnUnha = findViewById(R.id.ibUnha);

        ids = new ArrayList<>();
        idcateg = new ArrayList<>();

        buscar();
        dialogBuscando();

        //verifica se esta logado
        isLogado();


        btnBarba.setOnClickListener(v -> {
            if(mPermissoesConcedidas) {
                Intent intent = new Intent(this, SaloesActivity.class);
                this.openCategoriaIntent(intent, "Barba");
            }
        });

        btnCabelo.setOnClickListener(v -> {
            if(mPermissoesConcedidas) {
                Intent intent = new Intent(InicialActivity.this, SaloesActivity.class);
                this.openCategoriaIntent(intent, "Cabelo");
            }
        });

        btnDepilacao.setOnClickListener(v -> {
            if(mPermissoesConcedidas) {
                Intent intent = new Intent(InicialActivity.this, SaloesActivity.class);
                this.openCategoriaIntent(intent, "Depilação");
            }
        });

        btnOlho.setOnClickListener(v -> {
            if(mPermissoesConcedidas) {
                Intent intent = new Intent(InicialActivity.this, SaloesActivity.class);
                this.openCategoriaIntent(intent, "Olho");
            }
        });

        btnSobrancelha.setOnClickListener(v -> {
            if(mPermissoesConcedidas) {
                Intent intent = new Intent(InicialActivity.this, SaloesActivity.class);
                this.openCategoriaIntent(intent, "Sobrancelha");
            }
        });

        btnUnha.setOnClickListener(v -> {
            if(mPermissoesConcedidas) {
                Intent intent = new Intent(InicialActivity.this, SaloesActivity.class);
                this.openCategoriaIntent(intent, "Unha");
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void localizar(){
        if(mPermissoesConcedidas) {
            //para pegar dados de  localização via dispositivo
            LocationManager locationMangaer = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            LocationListener locationListener = new MyLocationListener();

            locationMangaer.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);

            localizacao = new MyServiceLocation(InicialActivity.this);

            if (!localizacao.canGetLocation()) {
                localizacao.showSettingsAlert();
            }
        }
    }

    private void buscar() {
        Query query = FirebaseDatabase.getInstance().getReference("servicos");

        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Servico servico = dataSnapshot.getValue(Servico.class);
                ids.add(servico.getMEstabId());
                idcateg.add(servico.getMCategoria());

               // myAdapter.notifyDataSetChanged();
                mProgressDialog.dismiss();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                // empty
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                // empty
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                // empty
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // empty
            }
        });
    }

    void dialogBuscando(){
        mProgressDialog = new ProgressDialog(InicialActivity.this);
        mProgressDialog.setMessage("Buscando...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgress(0);
        mProgressDialog.show();
    }

    public void isLogado() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            mProgressDialog.dismiss();
            abrirTelaPrincipal();

        }
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.app_bar_search) {

            Intent intetAbrirTelaLogin = new Intent(InicialActivity.this, TelaBuscaActivity.class);
            intetAbrirTelaLogin.putExtra("latitude", localizacao.getLatitude());
            intetAbrirTelaLogin.putExtra("longitude", localizacao.getLongitude());
            startActivity(intetAbrirTelaLogin);

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_cadastrar) {
            Intent intentAbrirTelaCadastro = new Intent(InicialActivity.this,CadastroBasicoActivity.class);
            startActivity(intentAbrirTelaCadastro);

        } else if (id == R.id.nav_logar) {
            Intent intetAbrirTelaLogin = new Intent(InicialActivity.this, LoginActivity.class);
            startActivity(intetAbrirTelaLogin);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void abrirTelaPrincipal() {
        Intent intentAbritPrincipal = new Intent(InicialActivity.this , ClienteLogadoActivity.class);
        startActivity(intentAbritPrincipal);
        InicialActivity.this.finish();
    }

    /*----------Listener class to get coordinates ------------- */
    private class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location loc) {

            //editLocation.setText("");
            //  pb.setVisibility(View.INVISIBLE);
            /*Toast.makeText(getBaseContext(),"Location changed : Lat: " +
                            loc.getLatitude()+ " Lng: " + loc.getLongitude(),
                    Toast.LENGTH_SHORT).show();*/
            String longitude = "Longitude: " +loc.getLongitude();
            Log.v(TAG, longitude);
            String latitude = "Latitude: " +loc.getLatitude();
            Log.v(TAG, latitude);

            /*----------to get City-Name from coordinates ------------- */
            String cityName=null;
            Geocoder gcd = new Geocoder(getBaseContext(),
                    Locale.getDefault());
            List<Address> addresses;
            try
            {
                addresses = gcd.getFromLocation(loc.getLatitude(), loc
                        .getLongitude(), 1);
                if (addresses.size() > 0) {
                    System.out.println(addresses.get(0).getLocality());
                    cityName=addresses.get(0).getLocality();
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            String s = longitude+"\n"+latitude +
                    "\n\nMy Currrent City is: "+cityName;
            //editLocation.setText(s);
            System.out.println("localização : "+s);
        }

        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onStatusChanged(String provider,
                                    int status, Bundle extras) {
            // TODO Auto-generated method stub
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(localizacao != null) localizacao.stopListener();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);

        for(int resultado : grantResults){
            if(resultado == PackageManager.PERMISSION_DENIED){
                mPermissoesConcedidas = false;
                alertaValidacaoPermissao();
            } else {
                mPermissoesConcedidas = true;
            }
        }
        if(mPermissoesConcedidas){localizar();}
    }

    private void alertaValidacaoPermissao(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissões negadas");
        builder.setMessage("É necessário aceitar as permissões");

        builder.setPositiveButton("OK", (dialogInterface, i) -> finish());

        AlertDialog dialog =  builder.create();
        dialog.show();
    }
}
