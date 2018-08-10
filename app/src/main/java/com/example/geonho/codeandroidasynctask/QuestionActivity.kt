@file:Suppress("DEPRECATION")

package com.example.geonho.codeandroidasynctask

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AlertDialog
import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONException
import kotlinx.android.synthetic.main.activity_question.*
import android.net.ConnectivityManager
import android.support.v4.app.ActivityCompat
import android.widget.Toast


class QuestionActivity : AppCompatActivity() {

    var questionList: MutableList<Question> = ArrayList()
    var index = -1
    var score = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)

        init()
        getQuestions().execute()
    }

    private fun init(){
        nextButton.isEnabled = false
        nextButton.alpha = 0.1.toFloat()
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    override fun onBackPressed() {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Are you sure !")
        dialog.setMessage("Do you want to stop?")
        dialog.setPositiveButton("Yes") {
            dialog, which -> dialog.dismiss()
            super.onBackPressed()
        }
        dialog.setNegativeButton("No"){
            dialog, which -> dialog.dismiss()
        }
        dialog.show()
    }

    fun updateQuestion() {
        val selected = choiceGroup.checkedRadioButtonId
        if (selected == -1) {
            Toast.makeText(this, "Please select a option.", Toast.LENGTH_SHORT).show()
            return
        }
        if (index < questionList.size) {
            when (selected) {
                choice1.id -> {
                    if (questionList[index].answer == 1)
                        score++
                }
                choice2.id -> {
                    if (questionList[index].answer == 2)
                        score++
                }
                choice3.id -> {
                    if (questionList[index].answer == 3)
                        score++
                }
                choice4.id -> {
                    if (questionList[index].answer == 4)
                        score++
                }
            }
            index++
            if (index < questionList.size) {
                questionText.text = questionList[index].question
                choice1.text = questionList[index].option1
                choice2.text = questionList[index].option2
                choice3.text = questionList[index].option3
                choice4.text = questionList[index].option4
                choiceGroup.clearCheck()
                if ((index + 1) == questionList.size)
                    nextButton.text = "Finish"
            } else {
                val preferences = PreferenceManager.getDefaultSharedPreferences(this)
                val editor = preferences.edit()
                editor.putInt("pastScore", score)
                editor.apply()
                startActivity(Intent(this@QuestionActivity, MainActivity::class.java))
                finish()
            }
        }
    }

    internal inner class getQuestions : AsyncTask<Void, Void, String>() {

        var hasInternet = false
        lateinit var progressDialog : ProgressDialog

        override fun onPreExecute() {
            super.onPreExecute()
            progressDialog = ProgressDialog(this@QuestionActivity)
            progressDialog.setMessage("Downloading Questions...")
            progressDialog.show()
        }

        override fun doInBackground(vararg params: Void?): String {
            return if (isNetworkAvailable()) {
                hasInternet = true
                val client = OkHttpClient()
                val url = resources.getString(R.string.server)
                val request = Request.Builder().url(url).build()
                val response = client.newCall(request).execute()
                response.body()?.string().toString()
            } else {
                "Please check your network connection."
            }
        }

        override fun onPostExecute(result: String?) {
            progressDialog.dismiss()
            if (hasInternet) {
                try {
                    val resultArray = JSONArray(result)
                    for (i in 0..(resultArray.length() - 1)) {
                        val currentObject = resultArray.getJSONObject(i)
                        val obj = Question()
                        obj.question = currentObject.getString("Question")
                        obj.option1 = currentObject.getString("Option1")
                        obj.option2 = currentObject.getString("Option2")
                        obj.option3 = currentObject.getString("Option3")
                        obj.option4 = currentObject.getString("Option4")
                        obj.answer = currentObject.getInt("Answer")
                        questionList.add(obj)
                    }
                    if (index == -1) {
                        index++
                        Log.d("result", "Question : " + questionList[index].question)
                        questionText.text = questionList[index].question
                        choice1.text = questionList[index].option1
                        choice2.text = questionList[index].option2
                        choice3.text = questionList[index].option3
                        choice4.text = questionList[index].option4
                    } else {
                        Log.d("result", "index : $index")
                    }

                    nextButton.isEnabled = true
                    nextButton.alpha = 1.toFloat()
                    nextButton.setOnClickListener {
                        updateQuestion()
                    }
                    Log.d("result", "result : $result")
                } catch (e: JSONException) {
                    Log.d("result", "JSONException result : $result")
                } catch (e: ClassCastException) {
                    Log.d("result", "ClassCastException result : $result")
                }
            }else{
                val dialog = AlertDialog.Builder(this@QuestionActivity)
                dialog.setTitle("Error")
                dialog.setMessage("$result")
                dialog.setPositiveButton("Close") {
                    dialog, which -> dialog.dismiss()
                    ActivityCompat.finishAffinity(this@QuestionActivity)
                }
                dialog.show()
            }
            super.onPostExecute(result)
        }
    }

}
