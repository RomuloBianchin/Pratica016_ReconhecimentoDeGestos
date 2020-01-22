package com.example.pratica016_reconhecimentodegestos;

import androidx.appcompat.app.AppCompatActivity;

import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


// Para atribuir um comportamento a cada gesto, a classe deve implementar um
//OnGesturePerformedListener comforme abaixo, e também é igualmente necessário implementar
// o método OnGesturePerformed(), este é apresentado como opção de correção do erro, na MainActivity.

public class MainActivity extends AppCompatActivity implements GestureOverlayView.OnGesturePerformedListener {

    GestureOverlayView gestureView; // Obejto responsável por reconhecer os gestos desenhados no ecrã.
    TextView nomeGesto; // Obejto responsável por exibir na lista os itens desenhados caso estejam entre 1 e 5.

    //É necessário “carregar” o ficheiro biblioteca aonde já foram pré desenhados os gestos, para que seja possível
    //compara-los com os gestos que serão desenhados.
    GestureLibrary gestLib;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Instancioando as váriaveis criadas anteriormente e associandos os mesmos aos seus Id´s.
        gestureView = (GestureOverlayView)findViewById(R.id.gestureOverlayView);
        nomeGesto = (TextView) findViewById(R.id.textView);

        //Instanciando a váriavel da bibliotéca, e informando o caminho que deve ser buscada as informações.
        gestLib = (GestureLibraries.fromRawResource(this,R.raw.gestures));

        // Em seguida, dentro do método onCreate, fazemos uma verificação se a biblioteca dos gestos foi carregada corretamente,
        // e caso a mesma não carregue, exibimos em um toast com esse alerta.
        if(!gestLib.load()){
            Toast.makeText(this,"Erro ao carregar a biblioteca",Toast.LENGTH_LONG).show();
        }
    }

    //O método onResume() liga o listener, ou seja, a aplicação fica à “escuta” do gesto.
    @Override
    protected void onResume() {
        super.onResume();
        gestureView.addOnGesturePerformedListener(this);
    }

    //O método onStop() “escuta” e liberta recursos do sistema, poupando a bateria do dispositivo.
    @Override
    protected void onStop() {
        gestureView.removeAllOnGestureListeners();
        super.onStop();
    }


    // No método onGesturePerformed, será escrito a estrutura do código que
    //será usado quando o gesto tiver sido desenhado e reconhecido para que o mesmo seja apresentado
    //o nome da etiqueta num campo TextView mais um alerta no Toast.
    @Override
    public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
        ArrayList<Prediction> previsoes = gestLib.recognize(gesture);
        if(previsoes.size() > 0){
            Prediction previsao = previsoes.get(0);

            if (previsao.score > 1.0){
                nomeGesto.append("|" + previsao.name);
                Toast.makeText(this,"" + previsao,Toast.LENGTH_LONG).show();
            }
            else{
                nomeGesto.append("");
                Toast.makeText(this,"Gesto desconhecido",Toast.LENGTH_LONG).show();

            }
        }
    }
}
