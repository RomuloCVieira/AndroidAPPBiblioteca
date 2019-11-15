package com.example.crudbiblioteca;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends ListActivity {

    EditText                            inputBuscar;
    Button                              btnAdicionar;
    Button                              btnBuscar;
    RadioButton                         radioAutor;
    RadioButton                         radioTitulo;
    RadioButton                         radioEditora;
    ArrayList<HashMap<String,String>>   listarLivros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //verificação para ver se tem permissao para gravar no disco

        verificarPermissao(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        btnAdicionar        = findViewById(R.id.btnAdicionar);
        btnBuscar           = findViewById(R.id.btnBuscar);
        inputBuscar         = findViewById(R.id.inputBuscar);
        radioEditora        = findViewById(R.id.radioEditora);
        radioAutor          = findViewById(R.id.radioAutor);
        radioTitulo         = findViewById(R.id.radioTitulo);

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buscarLivros();
            }
        });

        btnAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //criar o caminho para a tela de cadastro
                Intent tela = new Intent(MainActivity.this , CadastroDeLivro.class);

                //abrir tela
                startActivity(tela);
            }
        });

    }
    @Override
    protected void onResume() {
        super.onResume();

       //buscar todos os Livros cadastrados
       buscarLivros();
    }

    private void buscarLivros() {
        String autor         = "";
        String titulo        = "";
        String editora       = "";

        //verificar qual radio esta selecionado

        if(radioAutor.isChecked()){
            autor = inputBuscar.getText().toString();
        }else if (radioTitulo.isChecked()) {
            titulo = inputBuscar.getText().toString();
        }else if (radioEditora.isChecked()) {
            editora = inputBuscar.getText().toString();
        }else {

        }

        //instanciando objeto tabela livros
        Livro livro = new Livro (this);

        //listarLivro vai receber o metodo buscar passando o parametro para busca
        listarLivros = livro.buscar(autor,titulo,editora);

        ListAdapter adapter = new SimpleAdapter(this
                                                ,listarLivros
                                                ,R.layout.listview_modelo
                                                ,new String[] {"autor", "titulo" , "editora"}
                                                ,new int[] {R.id.lblAutor,R.id.lblTitulo,R.id.lblEditora}
                                                );

        setListAdapter(adapter);
    }

    private void verificarPermissao(String nomePermissao) {
        //verificar se o usuario ja deu permissao
        if(ContextCompat.checkSelfPermission(this,nomePermissao) != PackageManager.PERMISSION_GRANTED) {
            //if (ActivityCompat.shouldShowRequestPermissionRationale(this,nomePermissao));

            ActivityCompat.requestPermissions(this,new String[] {nomePermissao},0);
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent tela = new Intent(MainActivity.this,CadastroDeLivro.class);

        HashMap<String,String> livro = listarLivros.get(position);

        Bundle parans = new Bundle();

        parans.putString("autor",livro.get("autor"));
        parans.putString("titulo",livro.get("titulo"));
        parans.putString("editora",livro.get("editora"));

        tela.putExtras(parans);

        startActivity(tela);
    }
}
