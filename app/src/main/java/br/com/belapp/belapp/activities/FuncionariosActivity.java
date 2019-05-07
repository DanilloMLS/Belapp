package br.com.belapp.belapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

import br.com.belapp.belapp.R;
import br.com.belapp.belapp.database.utils.FirebaseUtils;
import br.com.belapp.belapp.model.Agendamento;
import br.com.belapp.belapp.model.Estabelecimento;
import br.com.belapp.belapp.model.Profissional;
import br.com.belapp.belapp.model.Servico;
import br.com.belapp.belapp.presenter.FuncionarioAdapter;

import static br.com.belapp.belapp.servicos.PermissaoKt.verificarPermissaoRestritivo;

public class FuncionariosActivity extends AppCompatActivity implements FuncionarioAdapter.ItemClicked, FuncionarioAdapter.AgendarButtonClicked{

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter myAdapter;
    ArrayList<Profissional> profissionais;
    private ProgressDialog mProgressDialog;

    private Estabelecimento mEstabelecimento;
    private Servico mServico;
    private Agendamento mAgendamento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_funcionarios);
        // Configuraçao do toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_activity_profissionais);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.rvProfissionais);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        profissionais = new ArrayList<Profissional>();
        mEstabelecimento = (Estabelecimento) getIntent().getSerializableExtra("estabelecimento");
        mServico = (Servico) getIntent().getSerializableExtra("servico");
        if(getIntent().hasExtra("agendamento")){
            mAgendamento = (Agendamento) getIntent().getSerializableExtra("agendamento");
        }



        myAdapter = new FuncionarioAdapter(this, profissionais);
        recyclerView.setAdapter(myAdapter);

        buscar();
        dialogBuscando();
    }

    @Override
    public void onItemClicked(int index) {
        Toast.makeText(this, "Profissional: "+profissionais.get(index).getNome(),Toast.LENGTH_SHORT).show();
    }

    private void buscar(){
        Query query = FirebaseDatabase.getInstance().getReference("profissionais");

        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Profissional profissional = dataSnapshot.getValue(Profissional.class);
                if (profissional.getMId().equals(mServico.getMProfissionais())){
                    profissionais.add(profissional);
                }
                myAdapter.notifyDataSetChanged();
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
        mProgressDialog = new ProgressDialog(FuncionariosActivity.this);
        mProgressDialog.setMessage("Buscando...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgress(0);
        mProgressDialog.show();
    }

    @Override
    public void onAgendarButtonClicked(int index) {
        if(verificarPermissaoRestritivo(FuncionariosActivity.this)) {
            if (mAgendamento == null) {
                mAgendamento = new Agendamento();
            }
            mAgendamento.setMCliente(FirebaseUtils.getUsuarioAtual().getUid());
            mAgendamento.setMEstabelecimento(mEstabelecimento);
            mAgendamento.setMServico(mServico);
            mAgendamento.setMProfissional(profissionais.get(index));

            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable("agendamento", mAgendamento);
            intent.setClass(FuncionariosActivity.this, AgendarServicoActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    /*private void selProfissionais(String servico){
        //ArrayList<Profissional> profissionais = new ArrayList<Profissional>();
        for (int i = 0; i < ApplicationClass.servicos.size(); i++){
            if (ApplicationClass.servicos.get(i).getMId().equals(servico)){
                profissionais.add(ApplicationClass.servicos.get(i).getProfissionais());
            }
        }
    }*/

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
