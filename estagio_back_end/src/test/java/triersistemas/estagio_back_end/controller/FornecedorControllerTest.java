package triersistemas.estagio_back_end.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import triersistemas.estagio_back_end.dto.request.FornecedorPagedRequestDto;
import triersistemas.estagio_back_end.dto.request.FornecedorRequestDto;
import triersistemas.estagio_back_end.dto.response.FornecedorResponseDto;
import triersistemas.estagio_back_end.entity.Filial;
import triersistemas.estagio_back_end.entity.Fornecedor;
import triersistemas.estagio_back_end.enuns.SituacaoCadastro;
import triersistemas.estagio_back_end.services.FornecedorService;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = FornecedorController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class FornecedorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FornecedorService fornecedorService;

    @Autowired
    private ObjectMapper objectMapper;

    private Filial filial;

    private Fornecedor fornecedor;

    private FornecedorRequestDto requestDto;

    private FornecedorResponseDto responseDto;

    private final Long fornecedorId = 1L;

    @BeforeEach
    void setUp() {
        filial = new Filial();
        filial.setId(1L);

        requestDto = new FornecedorRequestDto(
                "Nome",
                "Razão",
                "52.353.295/0001-83",
                "(63) 3245-6887",
                "email@email.com",
                SituacaoCadastro.ATIVO,
                filial.getId());

        fornecedor = new Fornecedor(requestDto, filial);
    }

    @Nested
    class postFornecedorTest {

        @Test
        @DisplayName("Deve criar um fornecedor e retornar um ResponseEntity.ok")
        void postFornecedorTest_V1() throws Exception {

            responseDto = new FornecedorResponseDto(fornecedor);

            given(fornecedorService.addFornecedor(ArgumentMatchers.eq(requestDto))).willReturn(responseDto);

            ResultActions response = mockMvc.perform(post("/fornecedores/post")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDto)));

            response.andExpect(status().isOk())
                    .andExpect(content().json(objectMapper.writeValueAsString(requestDto)));
        }
    }

    @Nested
    class updateFornecedorTest {

        @Test
        @DisplayName("Deve atualizar uma filial e retornar um ResponseEntity.ok")
        void updateFornecedorTest_V1() throws Exception {

            var requestDtoUpdate = new FornecedorRequestDto(
                    "Nome Update",
                    "Razão Update",
                    "52.353.295/0001-83",
                    "(63) 3245-6887",
                    "email@email.com",
                    SituacaoCadastro.ATIVO,
                    filial.getId());

            responseDto = new FornecedorResponseDto(
                    1L,
                    "Nome Update",
                    "Razão Update",
                    "52.353.295/0001-83",
                    "(63) 3245-6887",
                    "email@email.com",
                    SituacaoCadastro.ATIVO,
                    filial.getId());

            given(fornecedorService.updateFornecedor(ArgumentMatchers.eq(fornecedorId), ArgumentMatchers.eq(requestDtoUpdate))).willReturn(responseDto);

            ResultActions response = mockMvc.perform(put("/fornecedores/update/{id}", fornecedorId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDtoUpdate)));

            response.andExpect(status().isOk())
                    .andExpect(content().json(objectMapper.writeValueAsString(requestDtoUpdate)))
                    .andExpect(jsonPath("$.nomeFantasia").value("Nome Update"))
                    .andExpect(jsonPath("$.razaoSocial").value("Razão Update"));
        }
    }

    @Nested
    class deleteFornecedorTest {

        @Test
        @DisplayName("Deve deletar o fornecedor pelo id e retornar um ResponseEntity.ok")
        void deleteFornecedorTest_V1() throws Exception {

            var responseDto = new FornecedorResponseDto(fornecedor);

            given(fornecedorService.deleteFornecedor(ArgumentMatchers.eq(fornecedorId))).willReturn(responseDto);

            ResultActions response = mockMvc.perform(delete("/fornecedores/delete/{id}", fornecedorId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDto)));

            response.andExpect(status().isOk());
            response.andExpect(content().json(objectMapper.writeValueAsString(requestDto)));
        }
    }

    @Nested
    class getFornecedorByIdTest {

        @Test
        @DisplayName("Deve encontrar um fornecedor pelo id e retornar um ResponseEntity.ok")
        void getFornecedorByIdTest_V1() throws Exception {
            var responseDto = new FornecedorResponseDto(fornecedor);

            given(fornecedorService.getFornecedorById(ArgumentMatchers.eq(fornecedorId))).willReturn(responseDto);

            ResultActions response = mockMvc.perform(get("/fornecedores/get/{id}", fornecedorId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDto)));

            response.andExpect(status().isOk())
                    .andExpect(content().json(objectMapper.writeValueAsString(requestDto)));
        }
    }

    @Nested
    class getFornecedorPagedTest {

        @Test
        @DisplayName("Deve retornar uma lista paginada com filtro")
        void getFornecedorPagedTest_V1() throws Exception {
            var filialId = 1L;
            Pageable pageable = PageRequest.of(0, 1);
            Page<FornecedorResponseDto> responseDtoList = new PageImpl<>(List.of(
                    responseDto = new FornecedorResponseDto(fornecedor)
            ));

            var pagedRequest = new FornecedorPagedRequestDto(
                    null,
                    null,
                    null,
                    null,
                    null,
                    SituacaoCadastro.ATIVO,
                    null,
                    null
            );

            given(fornecedorService.getFornecedorPaged(ArgumentMatchers.eq(filialId), ArgumentMatchers.eq(pagedRequest), ArgumentMatchers.eq(pageable))).willReturn(responseDtoList);

            ResultActions response = mockMvc.perform(put("/fornecedores/getAllPaged")
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("page", "0")
                    .param("size", "1")
                    .param("filialId", String.valueOf(filialId))
                    .content(objectMapper.writeValueAsString(pagedRequest)));

            response.andExpect(jsonPath("$.content").isArray())
                    .andExpect(jsonPath("$.content[0].situacaoCadastro").value("ATIVO"))
                    .andExpect(jsonPath("$.totalElements").value(responseDtoList.getTotalElements()))
                    .andExpect(jsonPath("$.totalPages").value(responseDtoList.getTotalPages()))
                    .andExpect(jsonPath("$.size").value(pageable.getPageSize()))
                    .andExpect(jsonPath("$.number").value(pageable.getPageNumber()));

        }
    }
}