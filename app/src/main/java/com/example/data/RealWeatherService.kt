package com.example.data

import com.example.model.DailyForecast
import com.example.model.WeatherAdvisory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object RealWeatherService {

    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .build()

    // Mandya, Karnataka Coordinates
    private const val LATITUDE = 12.52
    private const val LONGITUDE = 76.89

    private const val OPEN_METEO_URL =
        "https://api.open-meteo.com/v1/forecast?latitude=$LATITUDE&longitude=$LONGITUDE&current=temperature_2m,relative_humidity_2m,wind_speed_10m,precipitation,weather_code&daily=weather_code,temperature_2m_max,temperature_2m_min,precipitation_probability_max&timezone=Asia%2FKolkata"

    suspend fun fetchLiveWeather(): WeatherAdvisory = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder().url(OPEN_METEO_URL).build()
            val response = client.newCall(request).execute()

            if (response.isSuccessful) {
                val bodyStr = response.body?.string() ?: return@withContext getOfflineFallback(false)
                val json = JSONObject(bodyStr)

                val current = json.getJSONObject("current")
                val temp = current.getDouble("temperature_2m").toInt()
                val humidity = current.getInt("relative_humidity_2m")
                val wind = current.getDouble("wind_speed_10m").toInt()
                val precip = current.getDouble("precipitation")
                val weatherCode = current.getInt("weather_code")

                val conditionInfo = parseWmoCode(weatherCode, precip)

                // Daily forecast
                val daily = json.getJSONObject("daily")
                val daysArray = daily.getJSONArray("time")
                val maxTemps = daily.getJSONArray("temperature_2m_max")
                val minTemps = daily.getJSONArray("temperature_2m_min")
                val rainProbs = daily.optJSONArray("precipitation_probability_max")
                val dailyCodes = daily.getJSONArray("weather_code")

                val forecasts = mutableListOf<DailyForecast>()
                val daysLabels = listOf("Today", "Tomorrow", "Wed", "Thu", "Fri", "Sat", "Sun")

                for (i in 0 until minOf(5, daysArray.length())) {
                    val label = if (i < daysLabels.size) daysLabels[i] else "Day $i"
                    val maxT = maxTemps.getDouble(i).toInt()
                    val minT = minTemps.getDouble(i).toInt()
                    val prob = if (rainProbs != null && i < rainProbs.length()) rainProbs.getInt(i) else 20
                    val code = dailyCodes.getInt(i)
                    forecasts.add(
                        DailyForecast(
                            day = label,
                            tempMax = maxT,
                            tempMin = minT,
                            condition = parseWmoCode(code, 0.0).first,
                            rainProb = prob
                        )
                    )
                }

                val hasRain = precip > 0.5 || (forecasts.firstOrNull()?.rainProb ?: 0) > 50

                return@withContext WeatherAdvisory(
                    temp = temp,
                    humidity = humidity,
                    windKmh = wind,
                    condition = conditionInfo.first,
                    conditionKn = conditionInfo.second,
                    location = "Mandya, Karnataka ($LATITUDE° N, $LONGITUDE° E)",
                    alertEn = if (hasRain) "High probability of rain showers today. Safeguard harvested grain."
                    else "Fair sunny conditions. Good window for crop spraying and intercultural operations.",
                    alertKn = if (hasRain) "ಇಂದು ಮಳೆಯಾಗುವ ಸಾಧ್ಯತೆ ಹೆಚ್ಚಿದೆ. ಕೊಯ್ಲು ಮಾಡಿದ ಧಾನ್ಯಗಳನ್ನು ಮಳೆಯಿಂದ ರಕ್ಷಿಸಿ."
                    else "ಉತ್ತಮ ಬಿಸಿಲಿನ ವಾತಾವರಣ. ಕಳೆ ಕೀಳಲು ಮತ್ತು ರಸಗೊಬ್ಬರ ನೀಡಲು ಸೂಕ್ತ ಸಮಯ.",
                    irrigationAdviceEn = if (hasRain) "Hold irrigation today. Natural rainfall sufficient."
                    else "Normal furrow/drip irrigation recommended for vegetable and sugarcane crops.",
                    irrigationAdviceKn = if (hasRain) "ಇಂದು ನೀರುಣಿಸಬೇಡಿ. ಮಳೆಯ ನೀರು ಸಾಕಾಗುತ್ತದೆ."
                    else "ತರಕಾರಿ ಮತ್ತು ಕಬ್ಬಿನ ಬೆಳೆಗಳಿಗೆ ನಿಯಮಿತ ಹನಿ ನೀರಾವರಿ ನೀಡಿ.",
                    isLiveApi = true,
                    apiSource = "Open-Meteo Satellite & Ground Sensors (Live)",
                    forecasts = forecasts
                )
            } else {
                return@withContext getOfflineFallback(false)
            }
        } catch (e: Exception) {
            return@withContext getOfflineFallback(false)
        }
    }

    private fun getOfflineFallback(isLive: Boolean): WeatherAdvisory {
        return WeatherAdvisory(
            temp = 29,
            humidity = 72,
            windKmh = 14,
            condition = "Partly Cloudy with Light Breeze",
            conditionKn = "ಭಾಗಶಃ ಮೋಡ ಕವಿದ ವಾತಾವರಣ, ತಂಪಾದ ಗಾಳಿ",
            location = "Mandya, Karnataka",
            alertEn = "Network unavailable. Showing regional seasonal agricultural forecast.",
            alertKn = "ನೆಟ್‌ವರ್ಕ್ ಲಭ್ಯವಿಲ್ಲ. ಪ್ರಾದೇಶಿಕ ಹವಾಮಾನ ಮುನ್ಸೂಚನೆ ತೋರಿಸಲಾಗುತ್ತಿದೆ.",
            irrigationAdviceEn = "Inspect soil moisture before irrigating standing crops.",
            irrigationAdviceKn = "ಬೆಳೆಗಳಿಗೆ ನೀರುಣಿಸುವ ಮೊದಲು ಮಣ್ಣಿನ ತೇವಾಂಶ ಪರೀಕ್ಷಿಸಿ.",
            isLiveApi = isLive,
            apiSource = if (isLive) "Live Telemetry" else "Offline Regional Estimate"
        )
    }

    private fun parseWmoCode(code: Int, precip: Double): Pair<String, String> {
        return when (code) {
            0 -> Pair("Clear Sky", "ಸ್ವಚ್ಛ ಆಕಾಶ")
            1, 2, 3 -> Pair("Mainly Clear & Partly Cloudy", "ಭಾಗಶಃ ಮೋಡ ಕವಿದ ವಾತಾವರಣ")
            45, 48 -> Pair("Fog & Misty", "ದಟ್ಟ ಮಂಜು ಮುಸುಕಿದ ವಾತಾವರಣ")
            51, 53, 55 -> Pair("Light Drizzle", "ತುಂತುರು ಮಳೆ")
            61, 63, 65 -> Pair("Rain Showers", "ಮಳೆಯ ಸಿಂಚನ")
            80, 81, 82 -> Pair("Heavy Rain Showers", "ಭಾರಿ ಮಳೆಯ ಮುನ್ಸೂಚನೆ")
            95, 96, 99 -> Pair("Thunderstorm with Lightning", "ಗುಡುಗು ಸಹಿತ ಬಿರುಗಾಳಿ ಮಳೆ")
            else -> if (precip > 0.0) Pair("Scattered Showers", "ಸಾಧಾರಣ ಮಳೆ") else Pair("Partly Cloudy", "ಮೋಡ ಕವಿದ ವಾತಾವರಣ")
        }
    }
}
