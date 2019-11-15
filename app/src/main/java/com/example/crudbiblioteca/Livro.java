package com.example.crudbiblioteca;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.HashMap;

public class Livro {
    public Livro(Context context) {
        //abrindo a conexao com o banco de dados
        BancoDeDados.getInstance().abrirBanco(context);

        //SQL para criar a tabela no banco de dados, caso ela ainda nao exista
        String sql = "CREATE TABLE IF NOT EXISTS livros (id INTEGER PRIMARY KEY AUTOINCREMENT" +
                ", autor TEXT" +
                ", titulo TEXT" +
                ", editora TEXT" +
                ", ano INTERGER" +
                ", numero_paginas INTERGER)";

        //executando o SQL no banco de dados
        BancoDeDados.getInstance().executarSQL(sql);

        //adicionando a coluna ESTADO do tipo TEXT na tabela TBALUNO
        BancoDeDados.getInstance().adicionarNovaColuna("livros",
                "gazin", "TEXT", "");
    }

    private String addAspas(String texto) {
        //adicionando Aspas Simples antes e depois do texto
        return "'" + texto + "'";
    }

    public void salvar(String autor, String titulo, String editora) {
        //buscando no banco pelo RA passado por parametro
        ArrayList<HashMap<String,String>> lista = buscar(autor, "", "");
        System.out.println(lista + "------------------------------------------------------------------------------------");
        //se encontrou um aluno com este RA, chama o alterar, senao insere
        if (lista.size() > 0) {
            alterar(autor, titulo, editora);
        }
        else {
            inserir(autor, titulo, editora);
        }
    }

    private void inserir(String autor, String titulo, String editora) {
        System.out.println(autor);
        System.out.println(titulo);
        System.out.println(editora);
        //montando SQL para inserir o aluno na tabela
        String sql = "INSERT INTO livros (autor, titulo, editora) VALUES (" + addAspas(autor) + ", " + addAspas(titulo) + ", " + addAspas(editora) + ")";

        //executando o SQL de INSERT no banco de dados
        BancoDeDados.getInstance().executarSQL(sql);

    }

    private void alterar(String autor, String titulo, String editora) {
        System.out.println(autor);
        System.out.println(titulo);
        System.out.println(editora);
        //montando SQL para editar o aluno na tabela
        String sql = "UPDATE livros SET " +
                " autor = " + addAspas(autor) + ", " +
                " titulo = " + addAspas(titulo) + ", " +
                " editora = " + addAspas(editora) + "" +
                "WHERE autor = " + addAspas(autor);

        //executando o SQL de UPDATE no banco de dados
        BancoDeDados.getInstance().executarSQL(sql);
        System.out.println("Atualizado com sucesso");

    }

    public void excluir(String autor) {
        //montando SQL para excluir o aluno na tabela
        String sql = "DELETE FROM livros WHERE autor = " + addAspas(autor);

        //executando o SQL de DELETE no banco de dados
        BancoDeDados.getInstance().executarSQL(sql);
    }

    public ArrayList<HashMap<String, String>> buscar(String autor, String titulo, String editora) {
        try {
            String condicaoSQL = "";
            String operadorSQL = "";

            //montando a condicao do SQL de acordo com os parametros recebidos
            if (autor.isEmpty() == false) {
                condicaoSQL = "UPPER(autor) LIKE UPPER(" +
                        addAspas("%" + autor + "%") + ")";
                operadorSQL = " AND ";
            }
            if (titulo.isEmpty() == false) {
                condicaoSQL += operadorSQL + "titulo = " + addAspas(titulo);
                operadorSQL = " AND ";
            }
            if (editora.isEmpty() == false) {
                condicaoSQL += operadorSQL + "UPPER(editora) LIKE UPPER(" +
                        addAspas("%" + editora + "%") + ")";
                operadorSQL = " AND ";
            }

            //criando uma busca usando o metodo QUERY do banco de dados
            Cursor cursor = BancoDeDados.getInstance().getBancoDados().query(
                    "livros", //nome da tabela
                    new String[]{"autor", "titulo", "editora"}, //campos retornados na busca
                    condicaoSQL, //condicao do WHERE da busca
                    null, //argumentos do WHERE, caso exista
                    null, //clausula GROUP BY
                    null, //clausula HAVING
                    "autor", //ORDER BY, ordenacao da busca
                    null //limite de registros
            );
            System.out.println(retornarLista(cursor) + "teste");
            //retornando a lista (array) com os registros do banco de dados
            return retornarLista(cursor);
        } catch (Exception e) {
            System.out.println("Erro ao buscar: " + e.getMessage());
            //caso de erro, retorna uma lista vazia
            return new ArrayList<HashMap<String, String>>();
        }
    }
        private ArrayList<HashMap<String, String>> retornarLista(Cursor cursor) {
            try {
                //criando a lista que sera retornada pelo metodo
                ArrayList<HashMap<String, String>> listaRetorno =
                        new ArrayList<HashMap<String, String>>();

                //pegando o indice de cada coluna do banco
                int campoAutor = cursor.getColumnIndex("autor");
                int campoTitulo = cursor.getColumnIndex("titulo");
                int campoEditora = cursor.getColumnIndex("editora");

                //se existem dados retornados do banco de dados
                if (cursor.getCount() > 0) {
                    //move o cursor para a primeira posicao
                    cursor.moveToFirst();

                    //FOR para rodas todos os itens do cursor (registros do banco)
                    for (int i = 0; i < cursor.getCount(); i++) {
                        HashMap<String,String> item = new HashMap<>();
                        //adicionando o valor do banco (cursor) no item HASH
                        item.put("autor", cursor.getString(campoAutor));
                        item.put("titulo", cursor.getString(campoTitulo));
                        item.put("editora", cursor.getString(campoEditora));

                        //adicionando o item na lista de retorno
                        listaRetorno.add(item);

                        //movendo o cursor para o proximo registro
                        cursor.moveToNext();
                    }
                }

                return listaRetorno;
            }
            catch (Exception e) {
                System.out.println("Erro ao montar lista: " + e.getMessage());
                //caso de erro, retorna uma lista vazia
                return new ArrayList<HashMap<String, String>>();
            }
        }
}
