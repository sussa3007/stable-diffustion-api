package com.preproject.server.stable.service;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.preproject.server.constant.ErrorCode;
import com.preproject.server.constant.Model;
import com.preproject.server.exception.GeneralException;
import com.preproject.server.stable.dto.StableDto;
import com.preproject.server.stable.dto.StableImageDto;
import com.preproject.server.stable.dto.StableResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StableService {

    private String apiUrl = "https://api.replicate.com/v1/predictions";

    private final Gson gson;

    public Map<String, Object> callAIModel(
            StableImageDto dto
    ) throws Throwable {
        URL url = new URL(apiUrl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setRequestProperty("Authorization", "Token " + dto.getKey());
        con.setRequestMethod("POST");

        con.setDoInput(true);
        con.setDoOutput(true); //POST 데이터를 OutputStream으로 넘겨 주겠다는 설정
        con.setUseCaches(false);
        con.setDefaultUseCaches(false);

        StableDto stableDto = new StableDto();
        stableDto.setInput(dto);
        if (dto.getModel().equals(Model.STABLE_DIFFUSION_V2.getName())) {
            stableDto.setVersion(Model.STABLE_DIFFUSION_V2.getVersion());
        } else if (dto.getModel().equals(Model.STABLE_DIFFUSION_V2_1.getName())) {
            stableDto.setVersion(Model.STABLE_DIFFUSION_V2_1.getVersion());
        } else if (dto.getModel().equals(Model.OPEN_JOURNEY.getName())) {
            stableDto.setVersion(Model.OPEN_JOURNEY.getVersion());
        } else if (dto.getModel().equals(Model.MID_JOURNEY.getName())) {
            stableDto.setVersion(Model.MID_JOURNEY.getVersion());
        } else if (dto.getModel().equals(Model.FUN_KO.getName())) {
            stableDto.setVersion(Model.FUN_KO.getVersion());
        } else if (dto.getModel().equals(Model.POKEMON.getName())) {
            stableDto.setVersion(Model.POKEMON.getVersion());
        } else {
            stableDto.setVersion(Model.STABLE_DIFFUSION_V2.getVersion());
        }


        String jsonMessage = gson.toJson(stableDto);

        OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
        wr.write(jsonMessage); //json 형식의 message 전달
        wr.flush();

        Map response = getResponse(gson, con);

        Map<String, Object> map = new HashMap<>();
        map.put("id", response.get("id"));
        map.put("model", dto.getModel());
        map.put("key", dto.getKey());
        return map;

    }

    public StableResponseDto getStableImageDto(
            String id, String model, String key
    ) throws IOException, InterruptedException {
        URL url = new URL(apiUrl +"/"+ id);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setRequestProperty("Authorization", "Token " + key);
        con.setRequestMethod("GET");

        Map map = getResponse(gson, con);
        if (map.get("status").equals("processing") || map.get("status").equals("starting")) {
            Thread.sleep(5000);
            return getStableImageDto(id, model, key);
        } else {
            System.out.println("status = " + map.get("status"));
            List<String> arr = (List) map.get("output");
            String output = arr.stream().findFirst().orElseThrow(() -> new RuntimeException());
            Map<String, Object> input = (Map<String, Object>) map.get("input");

            StableImageDto stableImageDto = StableImageDto.of(key, model, input);
            return StableResponseDto.of(stableImageDto, output);
        }
    }

    private Map getResponse(Gson gson, HttpURLConnection con) throws IOException {
        try {
            StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            br.close();
            return gson.fromJson(sb.toString(), Map.class);
        } catch (Exception e) {
            int responseCode = con.getResponseCode();
            if (responseCode == 401) {
                throw new GeneralException(ErrorCode.UNAUTHORIZED);
            } else if (responseCode == 402) {
                throw new GeneralException(ErrorCode.FREE_LIMIT_EXCEEDED);
            } else {
                throw new GeneralException(ErrorCode.BAD_REQUEST);
            }
        }

    }

}
