package br.com.belapp.belapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import br.com.belapp.belapp.R;
import br.com.belapp.belapp.model.Estabelecimento;

public class InfoActivity extends AppCompatActivity {

    private TextView tvInfoDescricao, tvInfoEndereco, tvInfoHorario,
            tvInfoTelefone;
    private ImageView ivFacebook, ivInstagram, ivGmail, ivSite;
    private String salao;
    private ProgressDialog mProgressDialog;
    private Estabelecimento estabelecimento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        Toolbar toolbar;
        ImageView ivTelefone;
        ImageView ivEndereco;
        toolbar = findViewById(R.id.tbInfoSalao);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        setSupportActionBar(toolbar);

        salao = getIntent().getStringExtra("salao");

        tvInfoDescricao = findViewById(R.id.tvInfoDescricao);
        tvInfoEndereco = findViewById(R.id.tvInfoEndereco);
        tvInfoHorario = findViewById(R.id.tvInfoHorario);
        tvInfoTelefone = findViewById(R.id.tvInfoTelefone);

        ivFacebook = findViewById(R.id.ivInfoFacebook);
        ivInstagram = findViewById(R.id.ivInfoInstagram);
        ivGmail = findViewById(R.id.ivInfoEmail);
        ivSite = findViewById(R.id.ivInfoSite);
        ivTelefone = findViewById(R.id.ivInfoTelefone);
        ivEndereco = findViewById(R.id.ivInfoEndereco);

        estabelecimento = new Estabelecimento();

        buscar();
        dialogBuscando();

        ivEndereco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirEndereco();
            }
        });

        ivTelefone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ligar();
            }
        });

        ivFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirFacebook();
            }
        });

        ivInstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirInstagram();
            }
        });

        ivSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirSite();
            }
        });

        ivGmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirGmail();
            }
        });
    }

    private void buscar(){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("estabelecimentos/"+salao);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                estabelecimento = dataSnapshot.getValue(Estabelecimento.class);
                mProgressDialog.dismiss();

                exibirInfo(estabelecimento);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(InfoActivity.this,"The read failed: "+databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void exibirInfo(Estabelecimento estab){
        String endereco = estab.getMRua()+", "+estab.getMNumero()+", "
                +estab.getMBairro()+", "+estab.getMCidade()+", "+estab.getMCep();
        tvInfoEndereco.setText(endereco);
        tvInfoDescricao.setText(estab.getMDescricao());
        tvInfoHorario.setText(estab.getMHorarios());
        tvInfoTelefone.setText(estab.getMTelefone());

        if (estab.getMLinkEmail().equals("-")){
            ivGmail.setVisibility(View.GONE);
        }
        if (estab.getMLinkSite().equals("-")){
            ivSite.setVisibility(View.GONE);
        }
        if (estab.getMLinkFacebook().equals("-")){
            ivFacebook.setVisibility(View.GONE);
        }
        if (estab.getMLinkInstagram().equals("-")){
            ivInstagram.setVisibility(View.GONE);
        }
    }

    private void dialogBuscando(){
        mProgressDialog = new ProgressDialog(InfoActivity.this);
        mProgressDialog.setMessage("Buscando...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgress(0);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
    }

    private void abrirEndereco(){
        Uri gmmIntentUri = Uri.parse("geo:"
                +estabelecimento.getMLatitude()+","
                +estabelecimento.getMLongitude()+"?q="
                +estabelecimento.getMRua()+", "
                +estabelecimento.getMNumero()+", "
                +estabelecimento.getMCidade());

        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }
    }

    private void ligar(){
        if (!estabelecimento.getMTelefone().equals("-")){
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+estabelecimento.getMTelefone()));
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }
    }

    private void abrirFacebook(){
        if (!estabelecimento.getMLinkFacebook().equals("-")){
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(estabelecimento.getMLinkFacebook()));
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }
    }

    private void abrirInstagram(){
        if (!estabelecimento.getMLinkInstagram().equals("-")){
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(estabelecimento.getMLinkInstagram()));
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }
    }

    private void abrirSite(){
        if (!estabelecimento.getMLinkSite().equals("-")){
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://"+estabelecimento.getMLinkSite()));
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }
    }

    private void abrirGmail(){
        if (!estabelecimento.getMLinkEmail().equals("-")){
            String addresses[] = new String[1];
            addresses[0] = estabelecimento.getMLinkEmail();
            String subject = "Dúvidas";
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:")); // only email apps should handle this
            intent.putExtra(Intent.EXTRA_EMAIL, addresses);
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
