package com.example.crudbiblioteca;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class BancoDeDados {
    //Singleton -> Intannciar a classe BancoDeDados na memoria, e garantir
    //  que ela sera instanciada uma unica vez.
    private static final BancoDeDados bancoDeDados = new BancoDeDados();
    public static BancoDeDados getInstance() {
        return bancoDeDados;
    }

    private SQLiteDatabase bancoDados;
    public SQLiteDatabase getBancoDados() {
        return bancoDados;
    }

    public void abrirBanco(Context context) {
        //verificando se o banco de dados ja esta conectado, se sim, para o processo
        if (bancoDados != null) {
            if (bancoDados.isOpen()) {
                return;
            }
        }

        //metodo que ira criar o banco de dados, caso nao exista,
        // se existir, abre o banco
        bancoDados = context.openOrCreateDatabase("dbAula.db",
                Context.MODE_PRIVATE, null);
    }

    public void fecharBanco() {
        //verifica se o banco esta nulo, se sim, para o processo
        if (bancoDados == null) {
            return;
        }

        //fechando a conexao do banco de dados
        bancoDados.close();
    }

    public void executarSQL(String sql) {
        try {
            //executando o SQL recebido por parametro no banco de dados
            bancoDados.execSQL(sql);
        }
        catch (Exception e) {
            System.out.println("Erro SQL: " + e.getMessage());
        }
    }

    public void adicionarNovaColuna(String tabela, String coluna,
                                    String tipo, String valorPadrao) {
        if (existeColunaTabela(tabela, coluna))
            return;

        String padrao = "";
        //verifica se existe um valor padrao, se sim, adiciona no SQL
        if (valorPadrao.isEmpty() == false)
            padrao = " DEFAULT " + valorPadrao;

        //adicionando a nova coluna da tabela no banco de dados
        executarSQL("ALTER TABLE " + tabela + " ADD COLUMN " +
                coluna + " " + tipo + " " + padrao);
    }

    private boolean existeColunaTabela(String tabela, String coluna) {
        try {
            //SELECT para trazer a estrutura da tabela sem dados
            Cursor cursor = bancoDados.rawQuery("SELECT * FROM " + tabela +
                    " LIMIT 0", null);

            //verifica se a coluna existe, se existir, retorna um numero, senao -1
            return (cursor.getColumnIndex(coluna) != -1);
            /*
            if (cursor.getColumnIndex(coluna) != -1)
                return true;
            else
                return false;
            */
        }
        catch (Exception e) {
            return false;
        }
    }
}
