package cos.corp.telegram.dto;

import java.util.List;

public class TelegramGetUpdatesResponse {

    private boolean ok;
    private List<TelegramUpdateDto> result;

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public List<TelegramUpdateDto> getResult() {
        return result;
    }

    public void setResult(List<TelegramUpdateDto> result) {
        this.result = result;
    }
}