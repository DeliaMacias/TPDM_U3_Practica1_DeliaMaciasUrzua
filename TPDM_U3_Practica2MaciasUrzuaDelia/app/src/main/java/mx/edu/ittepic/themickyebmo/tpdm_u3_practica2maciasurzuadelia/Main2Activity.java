package mx.edu.ittepic.themickyebmo.tpdm_u3_practica2maciasurzuadelia;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity {

    EditText nombre,precio,estatus,nomesa;
    Button insertar,consultar,eliminar,actualizar;
    ListView lista;
    List<Platillo> datosConsulta;
    List<String> allKey;
    DatabaseReference servicio;
    int posicion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        nombre=findViewById(R.id.nombre);
        precio=findViewById(R.id.precio);
        estatus=findViewById(R.id.estatus);
        nomesa=findViewById(R.id.nomesa);
        insertar=findViewById(R.id.insertar);
        consultar=findViewById(R.id.consultar);
        eliminar=findViewById(R.id.eliminar);
        actualizar=findViewById(R.id.actualizar);
        lista=findViewById(R.id.lista);
        posicion=0;

        servicio = FirebaseDatabase.getInstance().getReference();
        consultarPlatillo();

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                nombre.setText(datosConsulta.get(i).nombre);
                precio.setText(datosConsulta.get(i).precio);
                estatus.setText(datosConsulta.get(i).estatus);
                nomesa.setText(datosConsulta.get(i).nomesa);
                posicion=i;


            }
        });
        insertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertarPlatillo();
            }
        });
        consultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                consultarPlatillo();
            }
        });
        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eliminarPlatillo("platillo");
            }
        });
        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actualizarPlatillo("platillo",new Platillo(
                        nombre.getText().toString(),
                        precio.getText().toString(),
                        estatus.getText().toString(),
                        nomesa.getText().toString()));
            }
        });
    }

    private void actualizarPlatillo(final String rama, Platillo info) {
        servicio.child(rama).child(allKey.get(posicion)).setValue(info).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                minimensaje("EXITO","Se actualizó el platillo");
                nombre.setText("");
                precio.setText("");
                estatus.setText("");
                nomesa.setText("");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                minimensaje("Upps","No se pudo actualizar");
            }
        });



    }

    private void eliminarPlatillo(final String rama) {

                    AlertDialog.Builder c = new AlertDialog.Builder(Main2Activity.this);
                    c.setTitle("SEGURO?").setMessage("Quieres eliminar la venta de platillo\n"+nombre.getText().toString())
                            .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    servicio.child(rama).child(allKey.get(posicion)).removeValue()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    minimensaje("EXITO","Se elimino correctamento la venta platillo");
                                                    nombre.setText("");
                                                    precio.setText("");
                                                    estatus.setText("");
                                                    nomesa.setText("");
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    minimensaje("Upps!!","Error al eliminar"+e.getMessage());
                                                }
                                    });
                                }
                            }).setNegativeButton("Cancelar",null).show();





    }

    private void consultarPlatillo() {
        servicio.child("platillo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                datosConsulta = new ArrayList<>();
                allKey =new ArrayList<>();
                for (final DataSnapshot snap : dataSnapshot.getChildren()){
                    servicio.child("platillo").child(snap.getKey())
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    Platillo p = dataSnapshot.getValue(Platillo.class);
                                    if(p!=null){
                                        datosConsulta.add(p);
                                        allKey.add(dataSnapshot.getKey());
                                    }
                                    crearList();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void crearList () {
        String[] datPlatillo= new String[datosConsulta.size()];
        ArrayAdapter<String> adaptador;
        for(int i=0;i<datPlatillo.length;i++){
            Platillo pl = datosConsulta.get(i);
            datPlatillo[i]= pl.nombre+"\n"+pl.precio;
        }
        adaptador =  new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,datPlatillo);
        lista.setAdapter(adaptador);
    }

    private void minimensaje(String title, String message) {
        AlertDialog.Builder al = new AlertDialog.Builder(this);
        al.setTitle(title).setMessage(message).setPositiveButton("OK",null).show();
    }
    private void insertarPlatillo() {
        if (nombre.getText().toString().equals("")){
            minimensaje("Upps!!","Es importante que rellenes los campos obligatorios");
            return;
            }
        insertar("platillo",
                new Platillo(
                        nombre.getText().toString(),
                        precio.getText().toString(),
                        estatus.getText().toString(),
                        nomesa.getText().toString()));
    }

    private void insertar(final String rama,Platillo info) {
        servicio.child(rama).push()
                .setValue(info)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        minimensaje("EXITO","Se insertó el platillo");
                        nombre.setText("");
                        precio.setText("");
                        estatus.setText("");
                        nomesa.setText("");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        minimensaje("Upps","No se pudo insertar");
                    }
                });

    }
}
