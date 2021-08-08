package com.example.slagalica;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class PodesavanjaForma extends AppCompatActivity {
    private SeekBar            KlizacBrzina,
                               KlizacGranica;
    private Switch             SwitchNasumicnaSlika;
    private RecyclerView       TabelaSlikeBiranje;
    private Button             DugmeSacuvaj;
    private List<Integer>      SlikeId,
                               IzabranaSlika;
    private int                BrojSlika,
                               BrojUkljucenihSlika = -1,
                               INDEKS_SLIKE;
    private List<String>       NaziviSlika,
                               OpisiSlika;
    private List<Boolean>      UkljuceneSlike;
    private Boolean            NASUMICNA_SLIKA;
    private int                GR_POV,
                               MAX_KLIK;
    private AdapterPodesavanja ADAPTER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_podesavanja_forma);

        // NASE FUNKCIJE

        Inicijalizacija();
    }

    public void ProveraPredCuvanje(int indeks) {
        //if(!NASUMICNA_SLIKA) {
        INDEKS_SLIKE = indeks;
        CuvanjePodesavanja();
        //}
    }

    private void CuvanjePodesavanja() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);

        String ukljuceneSlikeStr = "";
        for(int i = 0; i < UkljuceneSlike.size(); i++) {
            ukljuceneSlikeStr += (UkljuceneSlike.get(i) == true)? "1" : "0";
        }

        intent.putExtra("UKLJUCENE_SLIKE_2", ukljuceneSlikeStr);
        intent.putExtra("GR_POV_2", GR_POV);
        Log.d("Put extra (2) >>", String.format("GR_POV: %d", GR_POV));
        intent.putExtra("KLIK_MAX_2", MAX_KLIK);
        intent.putExtra("NASUMICNA_SLIKA_2", NASUMICNA_SLIKA);
        intent.putExtra("INDEKS_SLIKE_2", INDEKS_SLIKE);

        startActivity(intent);
        //finish();
    }

    private void Inicijalizacija() {
        setTitle("Podešavanja ....");

        KlizacBrzina         = (SeekBar)      findViewById(R.id.klizacBrzina);
        KlizacGranica        = (SeekBar)      findViewById(R.id.klizacPovlacenje);
        SwitchNasumicnaSlika = (Switch)       findViewById(R.id.swNasumicnaSlika);
        TabelaSlikeBiranje   = (RecyclerView) findViewById(R.id.tabelaSlikeBiranje);
        DugmeSacuvaj         = (Button)       findViewById(R.id.dgmPodesavanja);

        BrojSlika            = 10;
        BrojUkljucenihSlika  = -1;
        INDEKS_SLIKE         = -1;
        IzabranaSlika        = new ArrayList<>();
        IzabranaSlika.add(-1);

        // INTENT SA GLAVNE FORME

        Intent intent       = getIntent();
        GR_POV              = intent.getIntExtra("GR_POV_1", 114);
        Log.d("Intent (2) <<", String.format("GR_POV: %d", GR_POV));
        MAX_KLIK            = intent.getIntExtra("KLIK_MAX_1", 214);
        NASUMICNA_SLIKA     = intent.getBooleanExtra("NASUMICNA_SLIKA_1", true);
        INDEKS_SLIKE        = intent.getIntExtra("INDEKS_SLIKE_1", -1);
        BrojUkljucenihSlika = intent.getIntExtra("BR_UKLJUCENIH_SLIKA_1", 1);

        InicijalizacijaNizaSlikeId();
        InicijalizacijaNizaNaziviSlika();
        InicijalizacijaNizaOpisiSlika();
        String ukljSlk = intent.getStringExtra("UKLJUCENE_SLIKE_1");
        UkljuceneSlike = InicijalizacijaNizaUkljuceneSlike(ukljSlk);

        KlizacBrzina.setProgress(MAX_KLIK);
        KlizacGranica.setProgress(GR_POV);
        SwitchNasumicnaSlika.setChecked(NASUMICNA_SLIKA);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        TabelaSlikeBiranje.setLayoutManager(layoutManager);
        ADAPTER = new AdapterPodesavanja(this, SlikeId, NaziviSlika, OpisiSlika, UkljuceneSlike, IzabranaSlika, BrojUkljucenihSlika);
        TabelaSlikeBiranje.setAdapter(ADAPTER);
        layoutManager.scrollToPosition(0);

        KlizacBrzina.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                MAX_KLIK = KlizacBrzina.getProgress();
                Toast.makeText(getApplicationContext(), String.format("%d", KlizacBrzina.getProgress()), Toast.LENGTH_SHORT).show();
            }
        });

        KlizacGranica.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                GR_POV = KlizacGranica.getProgress();
                Log.d("Klizac", String.format("GR_POV: %d", GR_POV));
                Toast.makeText(getApplicationContext(), String.format("%d", KlizacGranica.getProgress()), Toast.LENGTH_SHORT).show();
            }
        });

        SwitchNasumicnaSlika.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                NASUMICNA_SLIKA = SwitchNasumicnaSlika.isChecked();
            }
        });

        DugmeSacuvaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CuvanjePodesavanja();
            }
        });
    }

    private void InicijalizacijaNizaSlikeId() {
        SlikeId = new ArrayList<>();
        for(int i = 1; i <= BrojSlika; i++) {
            String ime_fajla = "thumb_" + String.format("%02d", i);
            SlikeId.add(getResources().getIdentifier(ime_fajla, "drawable", getPackageName()));
        }
    }

    private void InicijalizacijaNizaNaziviSlika() {
        NaziviSlika = new ArrayList<>();

        NaziviSlika.add("Sudoku");         // 1
        NaziviSlika.add("Taj Mahal");      // 2
        NaziviSlika.add("Tara");           // 3
        NaziviSlika.add("Foto aparat");    // 4
        NaziviSlika.add("Puzzle");         // 5
        NaziviSlika.add("Ključevi");       // 6
        NaziviSlika.add("Apstrakcija #1"); // 7
        NaziviSlika.add("Dezen #1");       // 8
        NaziviSlika.add("Optimus Prime");  // 9
        NaziviSlika.add("Rubikova kocka"); // 10
    }

    private void InicijalizacijaNizaOpisiSlika() {
        OpisiSlika = new ArrayList<>();

        OpisiSlika.add("TEŽINA: 1/10");  // 1
        OpisiSlika.add("TEŽINA: 2/10");  // 2
        OpisiSlika.add("TEŽINA: 3/10");  // 3
        OpisiSlika.add("TEŽINA: 3/10");  // 4
        OpisiSlika.add("TEŽINA: 6/10");  // 5
        OpisiSlika.add("TEŽINA: 7/10");  // 6
        OpisiSlika.add("TEŽINA: 8/10");  // 7
        OpisiSlika.add("TEŽINA: 10/10"); // 8
        OpisiSlika.add("TEŽINA: 2/10");  // 9
        OpisiSlika.add("TEŽINA: 1/10");  // 10
    }

    private List<Boolean> InicijalizacijaNizaUkljuceneSlike(String s) {
        List<Boolean> ukljuceneSlike = new ArrayList<>();

        for(int i = 0; i < s.length(); i++) {
            ukljuceneSlike.add(s.charAt(i) == '1');
        }

        return ukljuceneSlike;
    }
}
