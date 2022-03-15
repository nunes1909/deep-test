package com.example.requisicoeshttp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private Button botaoRecuperar;
    private TextView textoResultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textoResultado = findViewById(R.id.textResultado);
        botaoRecuperar = findViewById(R.id.botaoRecuperarDados);
        botaoRecuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String apiPrices = "https://blockchain.info/ticker";

                int cep = 92110000;
                //String apiCep = "https://viacep.com.br/ws/"+cep+"/json/";
                String apiCep = "https://viacep.com.br/ws/92120000/json/";

                MyTask task = new MyTask();
                task.execute(apiPrices);
            }
        });
    }

    class MyTask extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String stringUrl = strings[0];
            InputStream inputStream = null;
            InputStreamReader inputStreamReader = null;
            StringBuffer buffer = null;

            try {
                URL url = new URL(stringUrl);
                //basicamente cria a requisição
                HttpURLConnection conexao = (HttpURLConnection) url.openConnection();

                //Recupera os dados em Bytes
                inputStream = conexao.getInputStream();

                //Recupera os dados em Bytes e converte para char
                inputStreamReader = new InputStreamReader(inputStream);

                //Possibilita a leitura dos caracteres e converte de char para strings
                BufferedReader reader = new BufferedReader(inputStreamReader);
                buffer = new StringBuffer();
                String linha = "";
                while ( (linha = reader.readLine()) != null ){
                    buffer.append(linha);
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return buffer.toString();
        }

        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);

            String objetoValor = null;

            //Convertendo para formato Json
//            String enderecoCompleto = null;
//            String logradouro = null;
//            String cep = null;
//            String complemento = null;
//            String bairro = null;
//            String localidade = null;
//            String uf = null;

            String valorMoeda = null;
            String simbolo = null;

            try {
//                JSONObject jsonObject = new JSONObject(resultado);
//                logradouro = jsonObject.getString("logradouro");
//                cep = jsonObject.getString("cep");
//                complemento = jsonObject.getString("complemento");
//                bairro = jsonObject.getString("bairro");
//                localidade = jsonObject.getString("localidade");
//                uf = jsonObject.getString("uf");
//                enderecoCompleto = "Cep: " + cep + "\nRua: " + logradouro +
//                        "\nBairro: " + bairro + "\nCidade: " + localidade +
//                        "\nComplemento: " + complemento + "\nUF: " + uf;

                JSONObject jsonObject = new JSONObject(resultado);
                objetoValor = jsonObject.getString("BRL");
                JSONObject jsonObjectValorReal = new JSONObject(objetoValor);
                valorMoeda = jsonObjectValorReal.getString("last");
                simbolo = jsonObjectValorReal.getString("symbol");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            textoResultado.setText(simbolo + " " + valorMoeda);
        }
    }
}