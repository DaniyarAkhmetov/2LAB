package com.example.calculator;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tvExpression, tvResult;
    MaterialButton btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9;
    MaterialButton btnplus, btnminus, btnmult, btndivide;
    MaterialButton btnequals, btndot, btnac, btnopenbrackets, btnclosedbrackets;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvResult=findViewById(R.id.tvResult);
        tvExpression=findViewById(R.id.tvExpression);

        initButton(btn0, R.id.btn0);
        initButton(btn1, R.id.btn1);
        initButton(btn2, R.id.btn2);
        initButton(btn3, R.id.btn3);
        initButton(btn4, R.id.btn4);
        initButton(btn5, R.id.btn5);
        initButton(btn6, R.id.btn6);
        initButton(btn7, R.id.btn7);
        initButton(btn8, R.id.btn8);
        initButton(btn9, R.id.btn9);
        initButton(btnplus, R.id.btnplus);
        initButton(btnminus, R.id.btnminus);
        initButton(btnmult, R.id.btnmult);
        initButton(btndivide, R.id.btndivide);
        initButton(btnequals, R.id.btnequals);
        initButton(btndot, R.id.btndot);
        initButton(btnac, R.id.btnAC);
        initButton(btnopenbrackets, R.id.btnopenbrackets);
        initButton(btnclosedbrackets, R.id.btnclosedbrackets);






    }
    void initButton(MaterialButton button,int id){
        button = findViewById(id);
        button.setOnClickListener(this::onClick);
    }
    @Override
    public void onClick(View v) {
    MaterialButton button = (MaterialButton) v;
    String btnText = button.getText().toString();
    String data = tvExpression.getText().toString();

    if (btnText.equals("AC")) {
        tvExpression.setText("");
        tvResult.setText("");
        return;
    }
        if (btnText.equals("=")) {
            String finalResult = evaluateExpression(data); // Вычисляем результат
            tvResult.setText(finalResult); // Отображаем результат
            return; // Завершаем дальнейшую обработку
        }


        // Убираем начальный 0, если он есть и вводится число
        if (data.equals("0")) {
            if (btnText.matches("[0-9]")) {  // Если нажата цифра
                data = btnText;  // Заменяем 0 введенной цифрой
            } else {
                data += btnText;  // Иначе добавляем символ к 0 (например, оператор)
            }
        } else {
            // Если нет начального 0, просто добавляем символ
            data += btnText;
        }
        tvResult.setText(""); // Очищаем результат, пока мы вводим новое выражение

        tvExpression.setText(data);
        tvResult.setText("");

        String finalresult = evaluateExpression(data);
        if (!finalresult.equals("Error"))
             tvResult.setText(finalresult);

        Log.i("result", evaluateExpression(data)+ "");
    }

    private String evaluateExpression(String expression) {
        expression = expression.replace("×", "*").replace("÷", "/");
        if (expression.contains("/0")) {
            return "не определено"; // Возвращаем "не определено" при делении на ноль
        }
        // Создаем контекст Rhino
        Context rhino = Context.enter();
        // Устанавливаем версию JavaScript, которую будем использовать (по умолчанию актуальная)​
        rhino.setOptimizationLevel(-1); // Без оптимизации для мобильных устройств​
        try {
            // Создаем скриптовый объект Rhino​
            Scriptable scope = rhino.initStandardObjects();
            // Выполняем выражение JavaScript​
            String result = rhino.evaluateString(scope, expression, "JavaScript", 1, null).toString();
            // Приводим результат к числу и возвращаем его​

            DecimalFormat decimalFormat = new DecimalFormat("#.###");
            return decimalFormat.format(Double.parseDouble(result));

        }
        catch (Exception e) {
            return "Error";
        }

        finally {
            // Выход из контекста Rhino, освобождаем ресурсы​
            Context.exit();
        }
    }
}

