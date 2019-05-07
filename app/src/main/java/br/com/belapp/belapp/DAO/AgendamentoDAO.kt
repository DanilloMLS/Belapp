package br.com.belapp.belapp.DAO

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

import br.com.belapp.belapp.model.Agendamento

class AgendamentoDAO {
    var databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("agendamentos")

    fun save(agendamento: Agendamento?) {
        if (agendamento == null) {
            return
        }

        val mDatabase = databaseReference
        agendamento.mId = mDatabase.push().key
        mDatabase.child(agendamento.mId!!).setValue(agendamento)
    }

    fun remove(agendamento: Agendamento, completionListener: DatabaseReference.CompletionListener) {
        val mDatabase = databaseReference
        mDatabase.child(agendamento.mId!!).removeValue(completionListener)
    }

    fun update(agendamento: Agendamento) {
        val databaseReference = databaseReference
        databaseReference.child(agendamento.mId!!).setValue(agendamento)
    }
}
