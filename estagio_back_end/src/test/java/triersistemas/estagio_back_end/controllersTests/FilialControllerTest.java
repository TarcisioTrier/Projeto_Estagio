package triersistemas.estagio_back_end.controllersTests;

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
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import triersistemas.estagio_back_end.controller.FilialController;
import triersistemas.estagio_back_end.dto.request.FilialRequestDto;
import triersistemas.estagio_back_end.dto.response.FilialResponseDto;
import triersistemas.estagio_back_end.enuns.SituacaoContrato;
import triersistemas.estagio_back_end.exceptions.InvalidCnpjException;
import triersistemas.estagio_back_end.exceptions.InvalidFoneException;
import triersistemas.estagio_back_end.exceptions.NotFoundException;
import triersistemas.estagio_back_end.services.FilialService;
import triersistemas.estagio_back_end.validators.CnpjValidator;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FilialController.class)
public class FilialControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FilialService filialService;

    @MockBean
    private CnpjValidator cnpjValidator;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Teste post - Retorna ResponseOK")
    public void postFilial_ReturnOk() throws Exception {
        FilialRequestDto requestDto = new FilialRequestDto(
                "Nome Fantasia",
                "Razão Social",
                "12345678901234",
                "9999999999",
                "teste@example.com",
                SituacaoContrato.ATIVO,
                null);
        FilialResponseDto responseDto = new FilialResponseDto(
                1L,
                "Nome Fantasia",
                "Razão Social",
                "12345678901234",
                "9999999999",
                "teste@example.com",
                SituacaoContrato.ATIVO,
                null);

        when(filialService.addFilial(any(FilialRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/filiais/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nomeFantasia").value("Nome Fantasia"))
                .andExpect(jsonPath("$.razaoSocial").value("Razão Social"));

        verify(filialService).addFilial(any(FilialRequestDto.class));
    }

    @Test
    @DisplayName("Teste post - Retorna BadRequest com dados vazios")
    public void postFilial_InvalidData_ReturnBadRequest() throws Exception {
        FilialRequestDto requestDto = new FilialRequestDto(
                "",
                "",
                "",
                "",
                "",
                null,
                null);

        mockMvc.perform(MockMvcRequestBuilders.post("/filiais/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.nomeFantasia").value("Nome Fantasia é obrigatório"))
                .andExpect(jsonPath("$.razaoSocial").value("Razão Social é obrigatória"))
                .andExpect(jsonPath("$.cnpj").value("CNPJ é obrigatório"))
                .andExpect(jsonPath("$.telefone").value("Telefone é obrigatório"))
                .andExpect(jsonPath("$.email").value("Email é obrigatório"));
    }

    @Test
    @DisplayName("Teste post - Retorna BadRequest com cnpj inválido")
    public void postFilial_InvalidCnpj_ReturnBadRequest() throws Exception {
        FilialRequestDto requestDto = new FilialRequestDto(
                "Nome Fantasia",
                "Razão Social",
                "cnpj inválido",
                "9999999999",
                "teste@example.com",
                SituacaoContrato.ATIVO,
                null);

        when(filialService.addFilial(any(FilialRequestDto.class))).thenThrow(new InvalidCnpjException("CNPJ inválido"));

        mockMvc.perform(MockMvcRequestBuilders.post("/filiais/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("CNPJ inválido"));
    }

    @Test
    @DisplayName("Teste post - Retorna BadRequest com telefone inválido")
    public void postFilial_InvalidPhone_ReturnBadRequest() throws Exception {
        FilialRequestDto requestDto = new FilialRequestDto(
                "Nome Fantasia",
                "Razão Social",
                "12345678901234",
                "telefone inválido",
                "teste@example.com",
                SituacaoContrato.ATIVO,
                null);

        when(filialService.addFilial(any(FilialRequestDto.class))).thenThrow(new InvalidFoneException("Telefone inválido"));

        mockMvc.perform(MockMvcRequestBuilders.post("/filiais/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Telefone inválido"));
    }

    @Test
    @DisplayName("Teste post - Retorna BadRequest com email inválido")
    public void postFilial_invalidMail_ReturnBadRequest() throws Exception {
        FilialRequestDto requestDto = new FilialRequestDto(
                "Nome Fantasia",
                "Razão Social",
                "12345678901234",
                "9999999999",
                "Email inválido",
                SituacaoContrato.ATIVO,
                null);

        mockMvc.perform(MockMvcRequestBuilders.post("/filiais/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email").value("Email inválido"));
    }

    @Test
    @DisplayName("Teste getFilialById - Retorna ResponseOk")
    public void getFilialById_ShouldReturnOk() throws Exception {
        Long filialId = 1L;
        FilialResponseDto responseDto = new FilialResponseDto(filialId, "Nome Fantasia", "Razão Social", "12345678901234", "8888888888", "teste@example.com", SituacaoContrato.ATIVO, null);

        when(filialService.getFilialById(filialId)).thenReturn(responseDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/filiais/get/{id}", filialId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(filialId))
                .andExpect(jsonPath("$.nomeFantasia").value("Nome Fantasia"))
                .andExpect(jsonPath("$.razaoSocial").value("Razão Social"));

        verify(filialService).getFilialById(filialId);
    }

    @Test
    @DisplayName("Teste getFilialById - Retorna NotFoundException")
    public void getFilialById_NoExistentId_ReturnNotFound() throws Exception {
        Long nonExistentId = 999L;

        when(filialService.getFilialById(nonExistentId)).thenThrow(new NotFoundException("Filial not found"));

        mockMvc.perform(MockMvcRequestBuilders.get("/filiais/get/{id}", nonExistentId))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Filial not found"));
    }

    @Test
    @DisplayName("Teste getFilialFilter - Retorna ResponseOk")
    public void getFilialFilter_ReturnOk() throws Exception {
        String nome = "Nome Fantasia";
        String cnpj = "12345678901234";
        Pageable pageable = PageRequest.of(0, 10);
        Page<FilialResponseDto> responsePage = new PageImpl<>(Collections.singletonList(
                new FilialResponseDto(1L, nome, "Razão Social", cnpj, "8888888888", "teste@example.com", SituacaoContrato.ATIVO, null)
        ));

        when(filialService.getFilialFilter(nome, cnpj, pageable)).thenReturn(responsePage);

        mockMvc.perform(MockMvcRequestBuilders.get("/filiais//getAllPaged")
                        .param("nome", nome)
                        .param("cnpj", cnpj)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].nomeFantasia").value(nome))
                .andExpect(jsonPath("$.content[0].cnpj").value(cnpj));

        verify(filialService).getFilialFilter(nome, cnpj, pageable);
    }

    @Test
    @DisplayName("Teste getFilialById - Retorna Empty ResponseOk")
    public void getFilialFilter_NoResults_ReturnOkEmptyContent() throws Exception {
        String nome = "NonExistentName";
        String cnpj = "12345678901234";
        Pageable pageable = PageRequest.of(0, 10);
        Page<FilialResponseDto> emptyPage = new PageImpl<>(Collections.emptyList());

        when(filialService.getFilialFilter(nome, cnpj, pageable)).thenReturn(emptyPage);

        mockMvc.perform(MockMvcRequestBuilders.get("/filiais//getAllPaged")
                        .param("nome", nome)
                        .param("cnpj", cnpj)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content").isEmpty())
                .andExpect(jsonPath("$.totalElements").value(0));

        verify(filialService).getFilialFilter(nome, cnpj, pageable);
    }

    @Test
    @DisplayName("Teste getFilialById - Retorna BadRequest com cnpj inválido")
    public void getFilialFilter_InvalidCnpj_ReturnBadRequest() throws Exception {
        String nome = "Test";
        String invalidCnpj = "invalid_cnpj";
        Pageable pageable = PageRequest.of(0, 10);

        when(filialService.getFilialFilter(nome, invalidCnpj, pageable))
                .thenThrow(new InvalidCnpjException("CNPJ inválido"));

        mockMvc.perform(MockMvcRequestBuilders.get("/filiais//getAllPaged")
                        .param("nome", nome)
                        .param("cnpj", invalidCnpj)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("CNPJ inválido"));

        verify(filialService).getFilialFilter(nome, invalidCnpj, pageable);
    }

    @Test
    @DisplayName("Teste updateFilial - Retorna ResponseOk")
    public void updateFilial_ReturnOk() throws Exception {
        Long filialId = 1L;
        FilialRequestDto requestDto = new FilialRequestDto("Nome Fantasia Updated", "Razão Social Updated", "12345678901234", "8888888888", "teste@example.com", SituacaoContrato.ATIVO, null);
        FilialResponseDto responseDto = new FilialResponseDto(filialId, "Nome Fantasia Updated", "Razão Social Updated", "12345678901234", "8888888888", "teste@example.com", SituacaoContrato.ATIVO, null);

        when(filialService.updateFilial(eq(filialId), any(FilialRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/filiais/update/{id}", filialId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(filialId))
                .andExpect(jsonPath("$.nomeFantasia").value("Nome Fantasia Updated"))
                .andExpect(jsonPath("$.razaoSocial").value("Razão Social Updated"));

        verify(filialService).updateFilial(eq(filialId), any(FilialRequestDto.class));
    }

    @Test
    @DisplayName("Teste updateFilial - Retorna NotFoundException")
    public void updateFilial_NoExistentId_ReturnNotFound() throws Exception {
        Long nonExistentId = 999L;
        FilialRequestDto requestDto = new FilialRequestDto("Nome Fantasia", "Razão Social", "12345678901234", "8888888888", "teste@example.com", SituacaoContrato.ATIVO, null);

        when(filialService.updateFilial(eq(nonExistentId), any(FilialRequestDto.class))).thenThrow(new NotFoundException("Filial not found"));

        mockMvc.perform(MockMvcRequestBuilders.put("/filiais/update/{id}", nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Filial not found"));
    }

    @Test
    @DisplayName("Teste updateFilial - Retorna BadRequest com dados vazios")
    public void updateFilial_InvalidData_ReturnBadRequest() throws Exception {
        Long filialId = 1L;
        FilialRequestDto requestDto = new FilialRequestDto("", "", "", "", "", null, null);

        mockMvc.perform(MockMvcRequestBuilders.put("/filiais/update/{id}", filialId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.nomeFantasia").value("Nome Fantasia é obrigatório"))
                .andExpect(jsonPath("$.razaoSocial").value("Razão Social é obrigatória"))
                .andExpect(jsonPath("$.cnpj").value("CNPJ é obrigatório"))
                .andExpect(jsonPath("$.telefone").value("Telefone é obrigatório"))
                .andExpect(jsonPath("$.email").value("Email é obrigatório"));
    }

    @Test
    @DisplayName("Teste updateFilial - Lança exceção com CNPJ inválido")
    public void updateFilial_InvalidCnpj_ThrowInvalidCnpjException() throws Exception {
        Long filialId = 1L;
        FilialRequestDto requestDto = new FilialRequestDto(
                "Nome Fantasia Updated",
                "Razão Social Updated",
                "CNPJ_INVALIDO",  // CNPJ inválido propositalmente
                "8888888888",
                "teste@example.com",
                SituacaoContrato.ATIVO,
                null
        );

        when(filialService.updateFilial(eq(filialId), any(FilialRequestDto.class)))
                .thenThrow(new InvalidCnpjException("CNPJ inválido"));

        mockMvc.perform(MockMvcRequestBuilders.put("/filiais/update/{id}", filialId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("CNPJ inválido"));
    }

    @Test
    @DisplayName("Teste updateFilial - Lança exceção com CNPJ já existente")
    public void updateFilial_ExistingCnpj_ThrowInvalidCnpjException() throws Exception {
        Long filialId = 1L;
        String existingCnpj = "12345678901234";
        FilialRequestDto requestDto = new FilialRequestDto(
                "Nome Fantasia Updated",
                "Razão Social Updated",
                existingCnpj,
                "8888888888",
                "teste@example.com",
                SituacaoContrato.ATIVO,
                null
        );

        when(filialService.updateFilial(eq(filialId), any(FilialRequestDto.class)))
                .thenThrow(new InvalidCnpjException("CNPJ já cadastrado para outra filial"));

        mockMvc.perform(MockMvcRequestBuilders.put("/filiais/update/{id}", filialId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("CNPJ já cadastrado para outra filial"));

        verify(filialService).updateFilial(eq(filialId), any(FilialRequestDto.class));
    }

    @Test
    @DisplayName("Teste updateFilial - Retorna BadRequest com telefone inválido")
    public void updateFilial_InvalidFone_ReturnBadRequest() throws Exception {
        Long filialId = 1L;
        FilialRequestDto requestDto = new FilialRequestDto("Nome Fantasia Updated", "Razão Social Updated", "123456789101112", "telefone Inválido", "teste@example.com", SituacaoContrato.ATIVO, null);

        when(filialService.updateFilial(eq(filialId), any(FilialRequestDto.class))).thenThrow(new InvalidFoneException("Telefone inválido"));

        mockMvc.perform(MockMvcRequestBuilders.put("/filiais/update/{id}", filialId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Telefone inválido"));

        verify(filialService).updateFilial(eq(filialId), any(FilialRequestDto.class));
    }

    @Test
    @DisplayName("Teste updateFilial - Retorna BadRequest com email inválido")
    public void updateFilial_InvalidMail_ReturnBadRequest() throws Exception {
        Long filialId = 1L;
        FilialRequestDto requestDto = new FilialRequestDto("Nome Fantasia Updated", "Razão Social Updated", "123456789101112", "telefone Inválido", "Email inválido", SituacaoContrato.ATIVO, null);

        mockMvc.perform(MockMvcRequestBuilders.put("/filiais/update/{id}", filialId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email").value("Email inválido"));

    }

    @Test
    @DisplayName("Teste deleteFilial - Retorna ResponseOk")
    public void deleteFilial_ReturnOk() throws Exception {
        Long filialId = 1L;

        doNothing().when(filialService).deleteFilial(filialId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/filiais/delete/{id}", filialId))
                .andExpect(status().isOk())
                .andExpect(content().string("filial deletada"));

        verify(filialService).deleteFilial(filialId);
    }

    @Test
    @DisplayName("Teste deleteFilial - Retorna NotFoundException")
    public void deleteFilial_NoExistentId_ReturnNotFound() throws Exception {
        Long nonExistentId = 999L;

        doThrow(new NotFoundException("Filial not found")).when(filialService).deleteFilial(nonExistentId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/filiais/delete/{id}", nonExistentId))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Filial not found"));
    }

    @Test
    @DisplayName("Teste getAllFiliais - Retorna ResponseOk")
    public void getAllFiliais_ReturnOk() throws Exception {
        List<FilialResponseDto> filialList = Arrays.asList(
                new FilialResponseDto(1L, "Nome Fantasia 1", "Razão Social 1", "12345678901234", "1111111111", "teste1@example.com", SituacaoContrato.ATIVO, null),
                new FilialResponseDto(2L, "Nome Fantasia 2", "Razão Social 2", "56789012345678", "2222222222", "teste2@example.com", SituacaoContrato.ATIVO, null)
        );

        when(filialService.getFilialFilter(null)).thenReturn(filialList);

        mockMvc.perform(MockMvcRequestBuilders.get("/filiais/getAllFilter"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nomeFantasia").value("Nome Fantasia 1"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].nomeFantasia").value("Nome Fantasia 2"));

        verify(filialService).getFilialFilter(null);
    }

    @Test
    @DisplayName("Teste getAllFiliais com filtro - Retorna ResponseOk")
    public void getAllFiliaisWithFilter_ReturnOk() throws Exception {
        String nomeFilter = "Nome";
        List<FilialResponseDto> filialList = Collections.singletonList(
                new FilialResponseDto(1L, "Nome Fantasia 1", "Razão Social 1", "12345678901234", "1111111111", "teste1@example.com", SituacaoContrato.ATIVO, null)
        );

        when(filialService.getFilialFilter(nomeFilter)).thenReturn(filialList);

        mockMvc.perform(MockMvcRequestBuilders.get("/filiais/getAllFilter")
                        .param("nome", nomeFilter))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nomeFantasia").value("Nome Fantasia 1"));

        verify(filialService).getFilialFilter(nomeFilter);
    }

    @Test
    @DisplayName("Teste getAllFiliais com filtro - Retorna lista vazia")
    public void getAllFiliaisWithFilter_ReturnEmptyList() throws Exception {
        String nomeFilter = "NonexistentName";
        List<FilialResponseDto> emptyList = Collections.emptyList();

        when(filialService.getFilialFilter(nomeFilter)).thenReturn(emptyList);

        mockMvc.perform(MockMvcRequestBuilders.get("/filiais/getAllFilter")
                        .param("nome", nomeFilter))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(filialService).getFilialFilter(nomeFilter);
    }

    @Test
    @DisplayName("Teste getAllFiliais paginado - Retorna ResponseOk")
    public void getAllFilialsPaged_ReturnOk() throws Exception {
        Page<FilialResponseDto> pagedResponse = new PageImpl<>(Arrays.asList(
                new FilialResponseDto(1L, "Nome Fantasia 1", "Razão Social 1", "12345678901234", "1111111111", "teste1@example.com", SituacaoContrato.ATIVO, null),
                new FilialResponseDto(2L, "Nome Fantasia 2", "Razão Social 2", "56789012345678", "2222222222", "teste2@example.com", SituacaoContrato.ATIVO, null)
        ));

        when(filialService.getFilialFilter(null, null, PageRequest.of(0, 10))).thenReturn(pagedResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/filiais/getAllPaged")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].nomeFantasia").value("Nome Fantasia 1"))
                .andExpect(jsonPath("$.content[1].id").value(2L))
                .andExpect(jsonPath("$.content[1].nomeFantasia").value("Nome Fantasia 2"));

        verify(filialService).getFilialFilter(null, null, PageRequest.of(0, 10));
    }

    @Test
    @DisplayName("Teste getAllFiliais paginado com filtros - Retorna ResponseOk")
    public void getAllFilialsPagedWithFilters_ReturnOk() throws Exception {
        String nomeFilter = "Nome";
        String cnpjFilter = "12345678901234";
        Page<FilialResponseDto> pagedResponse = new PageImpl<>(Collections.singletonList(
                new FilialResponseDto(1L, "Nome Fantasia 1", "Razão Social 1", "12345678901234", "1111111111", "teste1@example.com", SituacaoContrato.ATIVO, null)
        ));

        when(filialService.getFilialFilter(nomeFilter, cnpjFilter, PageRequest.of(0, 10))).thenReturn(pagedResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/filiais/getAllPaged")
                        .param("page", "0")
                        .param("size", "10")
                        .param("nome", nomeFilter)
                        .param("cnpj", cnpjFilter))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].nomeFantasia").value("Nome Fantasia 1"))
                .andExpect(jsonPath("$.content[0].cnpj").value("12345678901234"));

        verify(filialService).getFilialFilter(nomeFilter, cnpjFilter, PageRequest.of(0, 10));
    }
}