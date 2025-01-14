package com.faeterj.tcc.controller;

import java.io.IOException;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.faeterj.tcc.dto.CreateAtestadoDTO;
import com.faeterj.tcc.dto.CreateConsultaDTO;
import com.faeterj.tcc.dto.CreateReceitaDTO;
import com.faeterj.tcc.dto.RequestResponseDTO;
import com.faeterj.tcc.model.User;
import com.faeterj.tcc.service.AtestadoService;
import com.faeterj.tcc.service.ConsultaService;
import com.faeterj.tcc.service.ReceitaService;
import com.faeterj.tcc.service.UserService;

@RestController
public class ConsultaController {

    private final ConsultaService consultaService;
    private final UserService userService;
    private final AtestadoService atestadoService;
    private final ReceitaService receitaService;

    public ConsultaController(ConsultaService consultaService, UserService userService, AtestadoService atestadoService,
            ReceitaService receitaService) {
        this.consultaService = consultaService;
        this.userService = userService;
        this.atestadoService = atestadoService;
        this.receitaService = receitaService;
    }

    @PostMapping("/consulta")
    public ResponseEntity<RequestResponseDTO> criarConsulta(@RequestBody CreateConsultaDTO dto, JwtAuthenticationToken token) 
    {
        try 
        {
            User user = userService.acharUserPorId(UUID.fromString(token.getName()));
            consultaService.criarConsulta(dto, user);
            return ResponseEntity.status(HttpStatus.CREATED).body(new RequestResponseDTO("Consulta criada com sucesso.", 201));
        } 
        catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(new RequestResponseDTO(e.getReason(), e.getStatusCode().value()));
        }
    }

    @DeleteMapping("/consulta/{id}")
    public ResponseEntity<RequestResponseDTO> deleteConsulta(@PathVariable("id") Long idConsulta, JwtAuthenticationToken token) 
    {
        try 
        {
            User user = userService.acharUserPorId(UUID.fromString(token.getName()));
            consultaService.deletarConsulta(idConsulta, user);
            return ResponseEntity.status(HttpStatus.OK).body(new RequestResponseDTO("Consulta excluida com sucesso.", 200));
        } 
        catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(new RequestResponseDTO(e.getReason(), e.getStatusCode().value()));
        }
    }

    @PutMapping("/consulta/{id}")
    public ResponseEntity<RequestResponseDTO> editaConsulta(@PathVariable("id") Long idConsulta, @RequestBody CreateConsultaDTO dto, JwtAuthenticationToken token) 
    {
        try 
        {
            User user = userService.acharUserPorId(UUID.fromString(token.getName()));
            consultaService.editarConsulta(idConsulta, user, dto);
            return ResponseEntity.status(HttpStatus.OK).body(new RequestResponseDTO("Consulta Editada com sucesso.", 200));
        } 
        catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(new RequestResponseDTO(e.getReason(), e.getStatusCode().value()));
        }
    }

    @GetMapping("/consultas/paciente/{pacienteId}")
    public ResponseEntity<?> listaConsultasPaciente(@PathVariable("pacienteId") Long idPaciente, 
                                                    @RequestParam(value = "page", defaultValue = "0") int page, 
                                                    @RequestParam(value = "pageSize", defaultValue = "10") int pageSize, 
                                                    JwtAuthenticationToken token) 
    {
        try 
        {
            User user = userService.acharUserPorId(UUID.fromString(token.getName()));
            var listaConsultasPaciente = consultaService.listarConsultasPaciente(idPaciente, page, pageSize, user);
            return ResponseEntity.status(HttpStatus.OK).body(listaConsultasPaciente);
        } 
        catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(new RequestResponseDTO(e.getReason(), e.getStatusCode().value()));
        }
    }

    @GetMapping("/consultas")
    public ResponseEntity<?> listaConsultasUser(@RequestParam(value = "page", defaultValue = "0") int page, 
                                                @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                                JwtAuthenticationToken token) 
    {
        try 
        {
            User user = userService.acharUserPorId(UUID.fromString(token.getName()));
            var listaConsultasUser = consultaService.listarConsultasUser(user, page, pageSize);
            return ResponseEntity.status(HttpStatus.OK).body(listaConsultasUser);
        } 
        catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(new RequestResponseDTO(e.getReason(), e.getStatusCode().value()));
        }
    }

    @PostMapping("/criarAtestado/consulta/{idConsulta}")
    public ResponseEntity<RequestResponseDTO> criarAtestado(
            @PathVariable("idConsulta") Long idConsulta, 
            @RequestPart CreateAtestadoDTO dto,
            @RequestParam(value = "file", required = false) MultipartFile file,
            JwtAuthenticationToken token) throws IOException {
        try {
            User user = userService.acharUserPorId(UUID.fromString(token.getName()));
            atestadoService.criarAtestado(idConsulta, dto, file, user);
            return ResponseEntity.status(HttpStatus.CREATED).body(new RequestResponseDTO("Atestado criado com sucesso.", 201));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(new RequestResponseDTO(e.getReason(), e.getStatusCode().value()));
        }
    }

    @PutMapping("/editarAtestado/consulta/{idConsulta}")
    public ResponseEntity<RequestResponseDTO> editarAtestado(
            @PathVariable("idConsulta") Long idConsulta, 
            @RequestPart CreateAtestadoDTO dto,
            @RequestParam(value = "file", required = false) MultipartFile file,
            JwtAuthenticationToken token) throws IOException {
        try {
            User user = userService.acharUserPorId(UUID.fromString(token.getName()));            atestadoService.editarAtestado(idConsulta, dto, file, user);
            return ResponseEntity.status(HttpStatus.OK).body(new RequestResponseDTO("Atestado editado com sucesso.", 200));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(new RequestResponseDTO(e.getReason(), e.getStatusCode().value()));
        }
    }

    @DeleteMapping("/excluirAtestado/consulta/{idConsulta}")
    public ResponseEntity<RequestResponseDTO> excluirAtestado(@PathVariable("idConsulta") Long idConsulta, JwtAuthenticationToken token) 
    {
        try 
        {
            User user = userService.acharUserPorId(UUID.fromString(token.getName()));
            atestadoService.excluirAtestado(idConsulta, user);
            return ResponseEntity.status(HttpStatus.OK).body(new RequestResponseDTO("Atestado excluido com sucesso.", 200));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(new RequestResponseDTO(e.getReason(), e.getStatusCode().value()));
        }
    }

    @PostMapping("/criarReceita/consulta/{idConsulta}")
    public ResponseEntity<RequestResponseDTO> criarReceita(
            @PathVariable("idConsulta") Long idConsulta, 
            @RequestPart CreateReceitaDTO dto,
            @RequestParam(value = "file", required = false) MultipartFile file,
            JwtAuthenticationToken token) throws IOException {
        try {
            User user = userService.acharUserPorId(UUID.fromString(token.getName()));
            receitaService.criarReceita(idConsulta, dto, file, user);
            return ResponseEntity.status(HttpStatus.CREATED).body(new RequestResponseDTO("Receita criada com sucesso.", 201));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(new RequestResponseDTO(e.getReason(), e.getStatusCode().value()));
        }
    }

    @PutMapping("/editarReceita/consulta/{idConsulta}")
    public ResponseEntity<RequestResponseDTO> editarReceita(
            @PathVariable("idConsulta") Long idConsulta, 
            @RequestPart CreateReceitaDTO dto,
            @RequestParam(value = "file", required = false) MultipartFile file,
            JwtAuthenticationToken token) throws IOException {
        try {
            User user = userService.acharUserPorId(UUID.fromString(token.getName()));            
            receitaService.editarReceita(idConsulta, dto, file, user);
            return ResponseEntity.status(HttpStatus.OK).body(new RequestResponseDTO("Atestado editado com sucesso.", 200));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(new RequestResponseDTO(e.getReason(), e.getStatusCode().value()));
        }
    }

    @DeleteMapping("/excluirReceita/consulta/{idConsulta}")
    public ResponseEntity<RequestResponseDTO> excluirReceita(@PathVariable("idConsulta") Long idConsulta, JwtAuthenticationToken token) 
    {
        try 
        {
            User user = userService.acharUserPorId(UUID.fromString(token.getName()));
            receitaService.excluirReceita(idConsulta, user);
            return ResponseEntity.status(HttpStatus.OK).body(new RequestResponseDTO("Atestado excluido com sucesso.", 200));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(new RequestResponseDTO(e.getReason(), e.getStatusCode().value()));
        }
    }
    
        
}
