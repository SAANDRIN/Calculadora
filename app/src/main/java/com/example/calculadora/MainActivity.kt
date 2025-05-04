package com.example.calculadora

import android.graphics.Color
import android.os.Bundle
import android.util.Size
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.calculadora.databinding.ActivityMainBinding
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private var expressaoParaCalculo = ""
    private var expressaoVisivel = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        capturarExpressao()
        configurarBotoesExtras()
    }

    private fun capturarExpressao() {
        with(binding) {
            val botoes = listOf(
                btn0, btn1, btn2, btn3, btn4, btn5, btn6,
                btn7, btn8, btn9, btnSomar, btnSubtrair,
                btnMultiplicar, btnDividir, btnPorcentagem,
                btnVirgula
            )

            for (botao in botoes) {
                botao.setOnClickListener {
                    // Desestruturando o pair
                    val (valorCalculo, valorVisivelItem) = when (it.id) {
                        R.id.btn0 -> "0" to "0"
                        R.id.btn1 -> "1" to "1"
                        R.id.btn2 -> "2" to "2"
                        R.id.btn3 -> "3" to "3"
                        R.id.btn4 -> "4" to "4"
                        R.id.btn5 -> "5" to "5"
                        R.id.btn6 -> "6" to "6"
                        R.id.btn7 -> "7" to "7"
                        R.id.btn8 -> "8" to "8"
                        R.id.btn9 -> "9" to "9"
                        R.id.btnSomar -> "+" to " + "
                        R.id.btnSubtrair -> "-" to " - "
                        R.id.btnMultiplicar -> "*" to " x "
                        R.id.btnDividir -> "/" to " ÷ "
                        R.id.btnPorcentagem -> "/100" to " % "
                        R.id.btnVirgula -> "." to ","
                        else -> "" to ""
                    }

                    expressaoParaCalculo += valorCalculo
                    expressaoVisivel += valorVisivelItem

                    txtExpressao.text = expressaoVisivel
                    atualizarResultadoParcial()
                }
            }
        }
    }

    private fun configurarBotoesExtras() {
        with(binding) {
            btnIgualdade.setOnClickListener {
                try {
                    val resultado = ExpressionBuilder(expressaoParaCalculo).build().evaluate()

                    if (resultado.isInfinite() || resultado.isNaN()) {
                        throw ArithmeticException("Não é possível dividir por zero")
                    }

                    txtResultado.text = "= $resultado"
                    txtResultado.setTextColor(Color.WHITE)
                    txtExpressao.setTextColor(Color.GRAY)
                    txtResultado.visibility = View.VISIBLE
                } catch (e: ArithmeticException) {
                    txtResultado.text = e.message
                    txtResultado.setTextColor(Color.RED)
                    txtResultado.visibility = View.VISIBLE
                } catch (e: Exception) {
                    txtResultado.text = "Erro"
                    txtResultado.setTextColor(Color.RED)
                    txtResultado.visibility = View.VISIBLE
                }
            }


            btnLimpar.setOnClickListener {
                expressaoParaCalculo = ""
                expressaoVisivel = ""
                txtExpressao.text = ""
                txtResultado.text = ""
                txtResultado.visibility = View.GONE
            }

            btnApagar.setOnClickListener {
                if (expressaoParaCalculo.isNotEmpty() && expressaoVisivel.isNotEmpty()) {
                    expressaoParaCalculo = expressaoParaCalculo.dropLast(1)
                    expressaoVisivel = expressaoVisivel.dropLast(1)
                    txtExpressao.text = expressaoVisivel
                    atualizarResultadoParcial()
                }
            }
        }
    }

    private fun atualizarResultadoParcial() {
        try {
            val resultado = ExpressionBuilder(expressaoParaCalculo).build().evaluate()

            val resultadoFormatado = if (resultado % 1 == 0.0 && !expressaoParaCalculo.contains(".")) {
                resultado.toInt().toString()
            } else {
                resultado.toString()
            }

            binding.txtResultado.text = "= $resultadoFormatado"
            binding.txtResultado.setTextColor(Color.GRAY)
            binding.txtResultado.visibility = View.VISIBLE
        } catch (e: ArithmeticException) {
            binding.txtResultado.text = "Não é possível dividir por zero"
            binding.txtResultado.setTextSize(25f)
            binding.txtResultado.setTextColor(Color.RED)
            binding.txtResultado.visibility = View.VISIBLE
        } catch (e: Exception) {
            binding.txtResultado.text = ""
        }
    }




}


