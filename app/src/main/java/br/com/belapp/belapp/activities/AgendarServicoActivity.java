package br.com.belapp.belapp.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Locale;
import java.util.Objects;

import br.com.belapp.belapp.DAO.AgendamentoDAO;
import br.com.belapp.belapp.R;
import br.com.belapp.belapp.exceptions.ValidationException;
import br.com.belapp.belapp.model.Agendamento;
import br.com.belapp.belapp.model.HorarioAtendimento;
import br.com.belapp.belapp.utils.DateUtils;
import br.com.belapp.belapp.utils.StringUtils;

public class AgendarServicoActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

//    private static final String TAG = "belapp.activities";
    private EditText mEdtData;
    private Agendamento mAgendamento;
    private ArrayList<Agendamento> mAgendamentosCliente;
    private ArrayList<Agendamento> mAgendamentosProfissional;
    private boolean mDataNaoMudou;
    private Spinner mSpHorariosDisponiveis;
    private Collection<String> mHorariosDisponiveis;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agendar_servico);
        mAgendamentosCliente = new ArrayList<>();
        mAgendamentosProfissional = new ArrayList<>();
        // Configuraçao do toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_activity_agendar_servico);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        setSupportActionBar(toolbar);
        configurarDatePicker();

        mAgendamento = (Agendamento) getIntent().getSerializableExtra("agendamento");
        TextView tvServico = findViewById(R.id.tvServicoAgendar);
        TextView tvEstabelecimento = findViewById(R.id.tvEstabelecimentoAgendar);
        TextView tvProfissional = findViewById(R.id.tvProfissionalAgendar);
        TextView tvPreco = findViewById(R.id.tvPrecoAgendar);
        TextView tvDiasFuncionamento = findViewById(R.id.tvDiasFuncionamento);


        tvServico.setText(String.format(Locale.getDefault(), "Serviço: %s",
                mAgendamento.getMServico().getMNome()));
        tvPreco.setText(String.format(Locale.getDefault(), "Preço: %s",
                StringUtils.INSTANCE.getDinheiro(mAgendamento.getMServico().getMPreco())));
        tvProfissional.setText(String.format(Locale.getDefault(), "Profissional: %s",
                mAgendamento.getMProfissional().getNome()));
        tvEstabelecimento.setText(String.format(Locale.getDefault(), "Estabelecimento: %s",
                mAgendamento.getMEstabelecimento().getMNome()));

        tvDiasFuncionamento.setText(
                String.format("%s: %s", getString(R.string.app_dias_funcionamento),
                        mAgendamento.getMEstabelecimento().getDiasFuncionamento()));


        Button btnAlterarServico = findViewById(R.id.btnAlterarServico);
        btnAlterarServico.setOnClickListener(view -> {
            Intent intent = new Intent(AgendarServicoActivity.this, PagSalaoActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("agendamento", mAgendamento);
            intent.putExtras(bundle);
            startActivity(intent);
        });

        Button btnAgendar = findViewById(R.id.btnAgendar);
        btnAgendar.setOnClickListener(view -> {
            try {
                validar();
                adicionarDataEHoraNoAgendamento();
                AgendamentoDAO dao = new AgendamentoDAO();
                if(mAgendamento.getMId() != null){
                    dao.update(mAgendamento);
                }
                else{
                    dao.save(mAgendamento);
                }
                Toast.makeText(AgendarServicoActivity.this,
                        getString(R.string.sucess_agendamento_realizado), Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                intent.setClass(AgendarServicoActivity.this, ClienteLogadoActivity.class);
                startActivity(intent);
//                finish();
            } catch (ValidationException e) {
                Toast.makeText(AgendarServicoActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        configurarSpinnerHorarios();

        buscarAgendamentosCliente();
        bucarAgendamentosProfissional();
        mDataNaoMudou = true;
        if(mAgendamento.getMData() != null){
            mEdtData.setText(mAgendamento.getMData());
            filtrarHorarios();
            btnAlterarServico.setVisibility(View.VISIBLE);
        }
        else{
            btnAlterarServico.setVisibility(View.GONE);
        }
    }

    private void configurarSpinnerHorarios() {
        mSpHorariosDisponiveis = findViewById(R.id.spHorariosAgendar);

        mHorariosDisponiveis = new ArrayList<>();
        mHorariosDisponiveis.add(getString(R.string.app_selecionar));
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, new ArrayList<>(mHorariosDisponiveis));
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpHorariosDisponiveis.setAdapter(dataAdapter);
    }

    private void adicionarDataEHoraNoAgendamento() {
        mAgendamento.setMData(mEdtData.getText().toString());
        mAgendamento.setMHora(((String) mSpHorariosDisponiveis.getSelectedItem()));
    }

    private void configurarDatePicker() {
        mEdtData = findViewById(R.id.edtData);

        // Atribui a ação de escolha de data.
        mEdtData.setOnClickListener(new View.OnClickListener() {
            /**
             * Chamado quando  a view é clicada.
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                // Abre a janela de diálogo para a escolha da data.
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        AgendarServicoActivity.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.setThemeDark(false);
                dpd.vibrate(true);
                dpd.dismissOnPause(true);
                dpd.setFirstDayOfWeek(Calendar.MONDAY);
                dpd.setMinDate(now);
                dpd.setAccentColor(Color.parseColor("#260CE8"));
                //noinspection deprecation
                dpd.show(getFragmentManager(), "Selecione a data");
            }


        });
    }

    private void bucarAgendamentosProfissional() {
        Query query = FirebaseDatabase.getInstance().getReference("agendamentos").orderByChild("mProfissional");

        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                Agendamento agendamento = dataSnapshot.getValue(Agendamento.class);
                if (Objects.requireNonNull(agendamento).getMProfissional().equals(mAgendamento.getMProfissional())){
                    mAgendamentosProfissional.add(agendamento);
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {
                // empty
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                // empty
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String s) {
                // empty
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // empty
            }
        });
    }

    private void buscarAgendamentosCliente() {
        Query query = FirebaseDatabase.getInstance().getReference("agendamentos").orderByChild("mCliente");

        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                Agendamento agendamento = dataSnapshot.getValue(Agendamento.class);
                if (Objects.requireNonNull(agendamento).getMCliente().equals(mAgendamento.getMCliente())){
                    mAgendamentosCliente.add(agendamento);
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {
                // empty
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                // empty
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String s) {
                // empty
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // empty
            }
        });
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Calendar now = Calendar.getInstance();
        now.set(Calendar.YEAR, year);
        now.set(Calendar.MONTH, monthOfYear);
        now.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String DataSelecionada = DateUtils.INSTANCE.converterDataParaString(now);
        mEdtData.setText(DataSelecionada);
        //mEdtData.setText(String.format(Locale.getDefault(), "%d/%d/%d", dayOfMonth, (monthOfYear+1), year));
        filtrarHorarios();
    }

    private void checarMudancaData() {
        if(mAgendamento.getMData() != null && !mEdtData.getText().toString().equals(mAgendamento.getMData())){
            mDataNaoMudou = false;
        }
    }

    public void filtrarHorarios() {
        checarMudancaData();
        mHorariosDisponiveis.clear();
        mHorariosDisponiveis.add(getString(R.string.app_selecionar));

        // Obtém o horário de abertura e fechamento do estabelecimento para o dia selecionado
        // para o atendimento
        HorarioAtendimento horariosDiaSelecionado = obterHorarioFuncionamentoEstabelecimento();

        // gera os horários possíveis, considerando o horário de abertura e fechamento do estabelecimento
        // e a duração do procedimento
        for (int i = horariosDiaSelecionado.getMAbertura(); i < horariosDiaSelecionado.getMFechamento(); i += mAgendamento.getMServico().getMDuracao()){

            mHorariosDisponiveis.add(DateUtils.INSTANCE.getFormatoHora(i/60, i%60));
        }
        // agrupa os horários indisponíveis na agenda do cliente e do profissional
        Collection<String> horariosOcupadosCliente = obterListaHorarios(mAgendamentosCliente,mEdtData.getText().toString());
        Collection<String> horariosOcupadosProfissional = obterListaHorarios(mAgendamentosProfissional, mEdtData.getText().toString());

        // verifica os horários em comum entre cliente e profissional

        mHorariosDisponiveis.removeAll(horariosOcupadosCliente);
        mHorariosDisponiveis.removeAll(horariosOcupadosProfissional);
        ArrayList<String> arrayHorarios = new ArrayList<>(mHorariosDisponiveis);
        // se for um reagendamento, o horário agendado anteriormente também estará disponível
        if(mAgendamento.getMData() != null && mDataNaoMudou){
            arrayHorarios.remove(mAgendamento.getMHora());
            arrayHorarios.set(0, mAgendamento.getMHora());
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, arrayHorarios);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpHorariosDisponiveis.setAdapter(dataAdapter);

        // Valida se ha datas disponiveis
        try {
            validar();
        } catch (ValidationException e) {
            Toast.makeText(AgendarServicoActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
//            if(mHorariosDisponiveis.size() == 1) {
//                mEdtData.callOnClick();
//
//            }
        }
    }

    private void validar() throws ValidationException {
        if(TextUtils.isEmpty(mEdtData.getText().toString())){
            throw new ValidationException(getString(R.string.error_selecione_uma_data));
        }
        boolean isAberto = false;
        for(HorarioAtendimento horarioAtendimento: mAgendamento.getMEstabelecimento()
                .getMHorariosAtendimento()){
            if(horarioAtendimento!= null && horarioAtendimento.getMDiaFuncionamento() == DateUtils.INSTANCE.getDiaDaSemanaEmData(mEdtData.getText().toString())){
                isAberto = true;
            }
        }
        if(!isAberto){
            throw new ValidationException(getString(R.string.error_estabelecimento_fechado_na_data));
        }
        if(mHorariosDisponiveis.size() == 1){
            throw new ValidationException(getString(R.string.error_nao_ha_horario_disponivel_para_data));
        }

        if(((String) mSpHorariosDisponiveis.getSelectedItem()).equalsIgnoreCase(getString(R.string.app_selecionar))){
            throw new ValidationException(getString(R.string.error_selecione_um_horario));
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private ArrayList<String> obterListaHorarios(ArrayList<Agendamento> agendamentos, String data){
        ArrayList<String> horarios = new ArrayList<>();
        for (Agendamento a: agendamentos){
            if(a.getMData().equals(data)){
                horarios.add(a.getMHora());
            }
        }
        return horarios;
    }

    /**
     * Filtra o horário do estabelecimento para o dia selecionado para o atendimento.
     */
    private HorarioAtendimento obterHorarioFuncionamentoEstabelecimento(){
        HorarioAtendimento horariosDiaSelecionado = new HorarioAtendimento();
        for(HorarioAtendimento horarioAtendimento: mAgendamento.getMEstabelecimento()
                .getMHorariosAtendimento()){
            if(horarioAtendimento!= null
                    && horarioAtendimento.getMDiaFuncionamento() == DateUtils.INSTANCE.getDiaDaSemanaEmData(mEdtData.getText().toString())){
                horariosDiaSelecionado = horarioAtendimento;
            }
        }
        return horariosDiaSelecionado;
    }

    /**
     * Métodos para auxilixar nos testes envoltendo data nesta activity
     *
     */

    public void setData(String data){
        runOnUiThread(() -> {
            mEdtData.setText(data);
            filtrarHorarios();

        });

    }

    public Collection<Integer> getDiasFuncionamento(){
        Collection<Integer> dias = new ArrayList<>();
        for(HorarioAtendimento horarioAtendimento: mAgendamento.getMEstabelecimento().getMHorariosAtendimento()){
            if(horarioAtendimento != null) {
                dias.add(horarioAtendimento.getMDiaFuncionamento());
            }
        }
        return dias;
    }

    public String getDataAgendamento(){
        return mAgendamento.getMData();
    }

}

