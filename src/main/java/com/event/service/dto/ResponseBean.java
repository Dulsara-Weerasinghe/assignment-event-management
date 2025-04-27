package com.event.service.dto;

import com.event.service.util.varList.MessageVarList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Title: ResponseBean
 * Description: Global Response Class
 * Created by Pakeetharan Balasubramaniam on 2/20/2023
 * Email: pakeetharan_b@epiclanka.net
 * Company: Epic Lanka (Pvt) Ltd.
 * Java Version: 1.8
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseBean {
    private String status;
    private String message;
    private Object content;

    public void setResponse(String status) {
        this.status = status;
        switch (status) {
            case MessageVarList.RSP_SUCCESS:
                this.message = "Success";
                break;
            case MessageVarList.RSP_NO_DATA_FOUND:
                this.message = "No Data Found";
                break;
            case MessageVarList.RSP_NOT_AUTHORISED:
                this.message = "Unauthorised Action";
                break;
            case MessageVarList.RSP_TOKEN_EXPIRED:
                this.message = "Token Expired";
                break;
        }
    }
}
