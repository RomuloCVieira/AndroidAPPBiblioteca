package com.example.crudbiblioteca;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class CadastroDeLivro extends AppCompatActivity {
    EditText inputAutor, inputTitulo, inputEditora;
    Button btnCancelar, btnSalvar, btnExcluir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_livro);

        inputAutor = findViewById(R.id.inputAutor);
        inputTitulo = findViewById(R.id.inputTitulo);
        inputEditora = findViewById(R.id.inputEditora);
        btnSalvar = findViewById(R.id.btnSalvar);
        btnCancelar = findViewById(R.id.btnCancelar);
        btnExcluir = findViewById(R.id.btnExcluir);

        //capturando o caminho que foi utilizado para abrir a tela
        Intent caminhoTela = getIntent();
        if (caminhoTela != null) {
            //capturando os parametros enviados para esta tela
            Bundle parans = caminhoTela.getExtras();

            //verifica se existem parametros enviados
            if (parans != null) {
                //populando os campos da tela com os recebidos da principal
                inputAutor.setText(parans.getString("autor"));
                inputTitulo.setText(parans.getString("titulo"));
                inputEditora.setText(parans.getString("editora"));

                //desabilitando o RA, pois é nossa chave no banco
                inputAutor.setEnabled(false);
            }
        }

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ao cancelar, chama o evento de voltar do
                //  do android para fechar a tela
                onBackPressed();
            }
        });

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //validando o Nome, RA e Cidade, para nao salvar vazio
                if (inputAutor.getText().toString().isEmpty()) {
                    mostrarMensagem("O campo Autor é obrigatório!");
                    return;
                }
                if (inputTitulo.getText().toString().isEmpty()) {
                    mostrarMensagem("O campo Titulo é obrigatório!");
                    return;
                }
                if (inputEditora.getText().toString().isEmpty()) {
                    mostrarMensagem("O campo Editora é obrigatório!");
                    return;
                }

                try {
                    Livro livro = new Livro(
                            CadastroDeLivro.this);

                    //salvado os dados do aluno
                    livro.salvar(inputAutor.getText().toString(),inputTitulo.getText().toString(), inputEditora.getText().toString());

                    //fechando a tela ao salvar
                    onBackPressed();
                }
                catch (Exception e) { }
            }
        });

        btnExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //criando a mensagem de alerta
                AlertDialog.Builder alerta =
                        new AlertDialog.Builder(CadastroDeLivro.this);

                //adicionando um titulo a mensagem
                alerta.setTitle("Alunos ALFA");

                //adicionando a pergunta de validacao na mensagem
                alerta.setMessage("Deseja realmente excluir este livro?");

                //criando a opcao negativa, que so fecha a mensagem
                alerta.setNegativeButton("Não", null);

                //criando a opcao positiva, que ira excluir o aluno
                alerta.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Livro tbAluno = new Livro(CadastroDeLivro.this);

                        //excluindo o aluno do banco de dados pelo RA
                        tbAluno.excluir(inputAutor.getText().toString());

                        //fechando a tela ao excluir
                        onBackPressed();
                    }
                });

                //exibindo a mensagem na tela
                alerta.show();
            }
        });
    }

    private void mostrarMensagem(String texto) {
        //criando a classe da mensagem de alerta
        AlertDialog.Builder alerta = new AlertDialog.Builder(this);

        //adicionando um titulo para a mensagem
        alerta.setTitle("Alunos ALFA");

        //adicionando o texto recebido pelo metodo na mensagem/alerta
        alerta.setMessage(texto);

        //adicionando um botao de OK para fechar a mensagem
        alerta.setNeutralButton("OK", null);

        //exibindo a mensagem criada na tela
        alerta.show();
    }
}
