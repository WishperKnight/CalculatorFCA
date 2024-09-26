package ies.carrillo.calculatorfca.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Stack;

import ies.carrillo.calculatorfca.R;

public class MainActivity extends AppCompatActivity {

    private TextView display; // Text view to show calculator output
    private Stack<Double> numbers = new Stack<>(); // Use a Stack for operations
    private String currentOperator = ""; // Keep track of the current operator
    int duration = Toast.LENGTH_SHORT;
    String text = "Error";
    Toast toast = Toast.makeText(this, text, duration);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        display = findViewById(R.id.tvResults); // Get the display text view

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loadingComponents();
    }

    /**
     * Method to load UI components and set click listeners.
     */
    public void loadingComponents() {

        numbers(); // Add functionality to number buttons

        // Buttons for basic operations
        Button btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(v -> currentOperator = "+");
        Button btnSust = findViewById(R.id.btnSust);
        btnSust.setOnClickListener(v -> currentOperator = "-");
        Button btnMult = findViewById(R.id.btnMult);
        btnMult.setOnClickListener(v -> currentOperator = "*");
        Button btnDiv = findViewById(R.id.btnDiv);
        btnDiv.setOnClickListener(v -> currentOperator = "/");

        // Button to clear the display
        Button btnClear = findViewById(R.id.btnClear);
        btnClear.setOnClickListener(v -> {
            numbers.clear();
            currentOperator = "";
            display.setText("");
        });

        // Buttons for additional functionalities (implement logic later)
        Button btnPosNeg = findViewById(R.id.btnPosNeg);
        btnPosNeg.setOnClickListener(v -> {
            if (!numbers.isEmpty()) {
                double number = numbers.pop();
                numbers.push(-number);
                display.setText(String.valueOf(numbers.peek()));
            }
        });

        Button btnPorcent = findViewById(R.id.btnPercent);
        btnPorcent.setOnClickListener(v -> {
            if (!numbers.isEmpty() && numbers.size() >= 1) {
                double number = numbers.pop();
                double percent = number / 100;
                numbers.push(percent);
                display.setText(String.valueOf(percent));
            }
        });
        Button btnDot = findViewById(R.id.btnDot);
        // btnDot.setOnClickListener(v -> /* Implement functionality for decimal */);

        // Button to calculate the result
        Button btnResult = findViewById(R.id.btnResult);
        btnResult.setOnClickListener(v -> {
            if (!currentOperator.isEmpty() && numbers.size() >= 2) {
                double secondNumber = numbers.pop();
                double firstNumber = numbers.pop();
                double result = performOperation(firstNumber, secondNumber, currentOperator);
                numbers.push(result);
                display.setText(String.valueOf(result));
                currentOperator = "";
            }
        });
    }

    /**
     * Method to add functionality to number buttons.
     */
    private void numbers() {

        // Get all number buttons and set click listeners to add the number to the display
        for (int i = 0; i <= 9; i++) {
            int buttonId = getResources().getIdentifier("btn" + i, "id", getPackageName());
            Button btn = findViewById(buttonId);
            final double number = i;
            btn.setOnClickListener(v -> {
                numbers.push(number);
                display.setText(display.getText().toString() + number);
            });
        }
    }

    /**
     * Performs the specified operation on two numbers.
     * (Add error handling for division by zero)
     */
    private double performOperation(double firstNumber, double secondNumber, String operator) {
        switch (operator) {
            case "+":
                return firstNumber + secondNumber;
            case "-":
                return firstNumber - secondNumber;

            case "*":
                return firstNumber * secondNumber;
            case "/":
                if (secondNumber == 0) {
                    toast.setText("Division by zero");
                    toast.show();
                    throw new ArithmeticException("Division by zero");

                }
                return firstNumber / secondNumber;
            default:

                return 0; // Handle invalid operator (optional)
        }
    }
}