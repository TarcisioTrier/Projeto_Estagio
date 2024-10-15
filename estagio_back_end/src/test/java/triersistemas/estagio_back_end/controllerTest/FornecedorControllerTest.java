package triersistemas.estagio_back_end.controllerTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.JsonPath;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import triersistemas.estagio_back_end.controller.FornecedorController;
import triersistemas.estagio_back_end.dto.request.FilialRequestDto;
import triersistemas.estagio_back_end.dto.request.FornecedorRequestDto;
import triersistemas.estagio_back_end.dto.response.FornecedorResponseDto;
import triersistemas.estagio_back_end.enuns.SituacaoCadastro;
import triersistemas.estagio_back_end.exceptions.InvalidCnpjException;
import triersistemas.estagio_back_end.exceptions.InvalidFoneException;
import triersistemas.estagio_back_end.services.FornecedorService;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FornecedorController.class)
public class FornecedorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FornecedorService fornecedorService;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Teste post - Retorna ResponseOK")
    public void postFornecedor_ReturnOk() throws Exception {
        FornecedorRequestDto requestDto = new FornecedorRequestDto(
                "Nome Fantasia",
                "Razão Social",
                "12345678901234",
                "9999999999",
                "teste@example.com",
                SituacaoCadastro.ATIVO,
                1L);
        FornecedorResponseDto responseDto = new FornecedorResponseDto(
                1L,
                "Nome Fantasia",
                "Razão Social",
                "12345678901234",
                "9999999999",
                "teste@example.com",
                SituacaoCadastro.ATIVO,
                1L);

        when(fornecedorService.addFornecedor(any(FornecedorRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/fornecedores/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nomeFantasia").value("Nome Fantasia"))
                .andExpect(jsonPath("$.razaoSocial").value("Razão Social"));

        verify(fornecedorService).addFornecedor(any(FornecedorRequestDto.class));
    }

    @Test
    @DisplayName("Teste post - Retorna BadRequest com dados vazios")
    public void postFornecedor_InvalidData_ReturnBadRequest() throws Exception {
        FornecedorRequestDto requestDto = new FornecedorRequestDto(
                "",
                "",
                "",
                "",
                "",
                SituacaoCadastro.ATIVO,
                null);

        mockMvc.perform(MockMvcRequestBuilders.post("/fornecedores/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.nomeFantasia").value("Nome Fantasia é obrigatório"))
                .andExpect(jsonPath("$.razaoSocial").value("Razão Social é obrigatória"))
                .andExpect(jsonPath("$.cnpj").value("CNPJ é obrigatório"))
                .andExpect(jsonPath("$.telefone").value("Telefone é obrigatório"))
                .andExpect(jsonPath("$.email").value("Email é obrigatório"))
                .andExpect(jsonPath("$.filialId").value("ID da Filial é obrigatório"));
    }

    @Test
    @DisplayName("Teste post - Retorna BadRequest com cnpj inválido")
    public void postFornecedor_InvalidCnpj_ReturnBadRequest() throws Exception {
        FornecedorRequestDto requestDto = new FornecedorRequestDto(
                "Nome Fantasia",
                "Razão Social",
                "Cnpj inválido",
                "9999999999",
                "teste@example.com",
                SituacaoCadastro.ATIVO,
                1L);

        when(fornecedorService.addFornecedor(any(FornecedorRequestDto.class))).thenThrow(new InvalidCnpjException("CNPJ inválido"));


        mockMvc.perform(MockMvcRequestBuilders.post("/fornecedores/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("CNPJ inválido"));
    }

    @Test
    @DisplayName("Teste post - Retorna BadRequest com telefone inválido")
    public void postFornecedor_InvalidFone_ReturnBadRequest() throws Exception {
        FornecedorRequestDto requestDto = new FornecedorRequestDto(
                "Nome Fantasia",
                "Razão Social",
                "123456789101112",
                "Telefone inválido",
                "teste@example.com",
                SituacaoCadastro.ATIVO,
                1L);

        when(fornecedorService.addFornecedor(any(FornecedorRequestDto.class))).thenThrow(new InvalidFoneException("Telefone inválido"));


        mockMvc.perform(MockMvcRequestBuilders.post("/fornecedores/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Telefone inválido"));
    }

    @Test
    @DisplayName("Teste post - Retorna BadRequest com email inválido")
    public void postFornecedor_InvalidMail_ReturnBadRequest() throws Exception {
        FornecedorRequestDto requestDto = new FornecedorRequestDto(
                "Nome Fantasia",
                "Razão Social",
                "123456789101112",
                "9999999999",
                "Email inválido",
                SituacaoCadastro.ATIVO,
                1L);

        mockMvc.perform(MockMvcRequestBuilders.post("/fornecedores/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email").value("Email inválido"));
    }

    @Test
    @DisplayName("Teste getFornecedorById - Retorna ResponseOk")
    public void getFornecedorById_ShouldReturnOk() throws Exception {
        Long fornecedorId = 1L;
        FornecedorResponseDto responseDto = new FornecedorResponseDto(fornecedorId, "Nome Fantasia", "Razão Social", "12345678901234", "8888888888", "teste@example.com", SituacaoCadastro.ATIVO, 1L);

        when(fornecedorService.getFornecedorById(fornecedorId)).thenReturn(responseDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/fornecedores/get/{id}", fornecedorId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(fornecedorId))
                .andExpect(jsonPath("$.nomeFantasia").value("Nome Fantasia"))
                .andExpect(jsonPath("$.razaoSocial").value("Razão Social"));

        verify(fornecedorService).getFornecedorById(fornecedorId);
    }

    @Test
    @DisplayName("Teste getFornecedorFilter - Retorna ResponseOk")
    public void getFornecedorFilter_ReturnOk() throws Exception {
        String nome = "Test";
        String cnpj = "12345678901234";
        SituacaoCadastro situacaoCadastro = SituacaoCadastro.ATIVO;

        PageRequest pageable = PageRequest.of(0, 10);

        Page<FornecedorResponseDto> responsePage = new PageImpl<>(List.of(
                new FornecedorResponseDto(1L, "Nome Fantasia", "Razão Social", cnpj, "8888888888", "teste@example.com", situacaoCadastro, 1L),
                new FornecedorResponseDto(2L, "Nome Fantasia2", "Razão Social2", "123456789101112", "999999999", "teste2@example.com", situacaoCadastro, 1L)
        ));

        when(fornecedorService.getFornecedorPaged(nome, cnpj, situacaoCadastro, pageable)).thenReturn(responsePage);

        mockMvc.perform(MockMvcRequestBuilders.get("/fornecedores/getAllFilter")
                        .param("nome", nome)
                        .param("cnpj", cnpj)
                        .param("situacaoCadastro", situacaoCadastro.toString())
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].nomeFantasia").value("Nome Fantasia"))
                .andExpect(jsonPath("$.content[0].cnpj").value(cnpj));

        verify(fornecedorService).getFornecedorPaged(nome, cnpj, situacaoCadastro, pageable);
    }

    @Test
    @DisplayName("Teste getFornecedorFilter - Retorna Empty ResponseOk")
    public void getFornecedorFilter_NoResults_ReturnOkEmptyContent() throws Exception {
        String nome = "NonExistentName";
        String cnpj = "12345678901234";
        SituacaoCadastro situacaoCadastro = SituacaoCadastro.ATIVO;
        PageRequest pageable = PageRequest.of(0, 10);
        Page<FornecedorResponseDto> emptyPage = new PageImpl<>(Collections.emptyList());

        when(fornecedorService.getFornecedorPaged(nome, cnpj, situacaoCadastro, pageable)).thenReturn(emptyPage);

        mockMvc.perform(MockMvcRequestBuilders.get("/fornecedores/getAllFilter")
                        .param("nome", nome)
                        .param("cnpj", cnpj)
                        .param("situacaoCadastro", situacaoCadastro.toString())
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content").isEmpty())
                .andExpect(jsonPath("$.totalElements").value(0));

        verify(fornecedorService).getFornecedorPaged(nome, cnpj, situacaoCadastro, pageable);
    }

    @Test
    @DisplayName("Teste getFornecedorFilter - Retorna BadRequest com cnpj inválido")
    public void getFornecedorFilter_InvalidCnpj_ReturnBadRequest() throws Exception {
        String nome = "Test";
        String invalidCnpj = "invalid_cnpj";
        SituacaoCadastro situacaoCadastro = SituacaoCadastro.ATIVO;
        PageRequest pageable = PageRequest.of(0, 10);

        when(fornecedorService.getFornecedorPaged(nome, invalidCnpj, situacaoCadastro, pageable))
                .thenThrow(new InvalidCnpjException("CNPJ inválido"));

        mockMvc.perform(MockMvcRequestBuilders.get("/fornecedores/getAllFilter")
                        .param("nome", nome)
                        .param("cnpj", invalidCnpj)
                        .param("situacaoCadastro", situacaoCadastro.toString())
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("CNPJ inválido"));

        verify(fornecedorService).getFornecedorPaged(nome, invalidCnpj, situacaoCadastro, pageable);
    }

    @Test
    @DisplayName("Teste updateFornecedor - Retorna ResponseOk")
    public void updateFornecedor_ReturnOk() throws Exception {
        Long fornecedorId = 1L;
        FornecedorRequestDto requestDto = new FornecedorRequestDto("Nome Fantasia Updated", "Razão Social Updated", "12345678901234", "8888888888", "teste@example.com", SituacaoCadastro.ATIVO, 1L);
        FornecedorResponseDto responseDto = new FornecedorResponseDto(fornecedorId, "Nome Fantasia Updated", "Razão Social Updated", "12345678901234", "8888888888", "teste@example.com", SituacaoCadastro.ATIVO, 1L);

        when(fornecedorService.updateFornecedor(eq(fornecedorId), any(FornecedorRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/fornecedores/update/{id}", fornecedorId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(fornecedorId))
                .andExpect(jsonPath("$.nomeFantasia").value("Nome Fantasia Updated"))
                .andExpect(jsonPath("$.razaoSocial").value("Razão Social Updated"));

        verify(fornecedorService).updateFornecedor(eq(fornecedorId), any(FornecedorRequestDto.class));
    }

    @Test
    @DisplayName("Teste deleteFornecedor - Retorna ResponseOk")
    public void deleteFornecedor_ReturnOk() throws Exception {
        Long fornecedorId = 1L;

        doNothing().when(fornecedorService).alteraSituacao(fornecedorId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/fornecedores/situacao/{id}", fornecedorId))
                .andExpect(status().isOk())
                .andExpect(content().string("situação alterada"));

        verify(fornecedorService).alteraSituacao(fornecedorId);
    }
}