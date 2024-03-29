package com.acho.studyAws.web.dto;

import com.acho.studyAws.web.dto.HelloResponseDto;
import org.junit.Test;
import static org.assertj.core.api.Assertions.*;

public class HelloResponseDtoTest {

    @Test
    public void 룸복_기능_테스트() {
        //given
        String name = "test";
        int amount = 1000;

        //when
        HelloResponseDto dto = new HelloResponseDto(name, amount);

        //then
        assertThat(dto.getName()).isEqualTo(name);
        assertThat(dto.getAmount()).isEqualTo(amount);
    }
}