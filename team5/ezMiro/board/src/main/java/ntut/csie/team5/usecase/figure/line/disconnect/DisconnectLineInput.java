package ntut.csie.team5.usecase.figure.line.disconnect;

import ntut.csie.sslab.ddd.usecase.Input;

public interface DisconnectLineInput extends Input{

    String getLineId();

    void setLineId(String lineId);

    String getEndpointId();

    void setEndpointId(String endpointId);
}
