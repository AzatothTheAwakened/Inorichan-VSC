package EventsAndCommands.UtilityCommands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import org.json.JSONArray;


import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

public class TranslateCommand extends Command {

    public TranslateCommand() {
        this.name = "translate";
        this.help = "translates using google translator";
    }

    @Override
    protected void execute(CommandEvent event) {
        String args = event.getArgs();
        try {
            String textEncoded = URLEncoder.encode(args, StandardCharsets.UTF_8);
            String url = "https://translate.googleapis.com/translate_a/single?client=gtx&sl=auto&tl=en&dt=t&q=" + textEncoded;
            HttpResponse<String> response = Unirest.get(url)
                    .header("User-Agent", "Mozilla/5.0")
                    .header("cache-control", "no-cache")
                    .asString();

            JSONArray responseArray = new JSONArray(response.getBody());
            String translation = responseArray.getJSONArray(0).getJSONArray(0).getString(0);

            event.reply(translation);


        } catch (Exception e) {
            event.reply("Could not get response", msg -> msg.delete().queueAfter(3, TimeUnit.SECONDS));
        }


    }

}

