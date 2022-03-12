package br.com.controledeponto.exceptionHandler;

import br.com.controledeponto.exception.DeveHaverNoMinimoUmaHoraDeAlmocoException;
import br.com.controledeponto.exception.HorarioInferiorAoUltimoRegistradoException;
import br.com.controledeponto.exception.HorarioJaRegistradoException;
import br.com.controledeponto.exception.NaoEPossivelAlocarMaisHorasDoQueTrabalhadoException;
import br.com.controledeponto.exception.NaoHaRegistrosDeTrabalhoNoMesException;
import br.com.controledeponto.exception.NaoPodeHaverMaisDeQuatroRegistrosException;
import br.com.controledeponto.exception.NaoPodeRegistrarHorasEmFinalDeSemanaException;
import br.com.controledeponto.exception.NaoPossuiTempoDisponivelParaAlocacaoException;
import br.com.controledeponto.model.Erro;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.format.DateTimeParseException;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		return handleExceptionInternal(ex, new Erro("Campo obrigatório não informado"), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}

	@ExceptionHandler({ DateTimeParseException.class })
	public ResponseEntity<Object> handleDateTimeParseException(DateTimeParseException ex, WebRequest request) {
		return handleExceptionInternal(ex, new Erro("Data e hora em formato inválido"), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}

	@ExceptionHandler({ DeveHaverNoMinimoUmaHoraDeAlmocoException.class })
	public ResponseEntity<Object> handleDeveHaverNoMinimoUmaHoraDeAlmocoException(DeveHaverNoMinimoUmaHoraDeAlmocoException ex, WebRequest request) {
		return handleExceptionInternal(ex, new Erro(ex.getMessage()), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}

	@ExceptionHandler({ HorarioInferiorAoUltimoRegistradoException.class })
	public ResponseEntity<Object> handleHorarioInferiorAoUltimoRegistradoException(HorarioInferiorAoUltimoRegistradoException ex, WebRequest request) {
		return handleExceptionInternal(ex, new Erro(ex.getMessage()), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}

	@ExceptionHandler({ HorarioJaRegistradoException.class })
	public ResponseEntity<Object> handleHorarioJaRegistradoException(HorarioJaRegistradoException ex, WebRequest request) {
		return handleExceptionInternal(ex, new Erro(ex.getMessage()), new HttpHeaders(), HttpStatus.CONFLICT, request);
	}

	@ExceptionHandler({ NaoEPossivelAlocarMaisHorasDoQueTrabalhadoException.class })
	public ResponseEntity<Object> handleNaoEPossivelAlocarMaisHorasDoQueTrabalhadoException(NaoEPossivelAlocarMaisHorasDoQueTrabalhadoException ex, WebRequest request) {
		return handleExceptionInternal(ex, new Erro(ex.getMessage()), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}

	@ExceptionHandler({ NaoPodeHaverMaisDeQuatroRegistrosException.class })
	public ResponseEntity<Object> handleNaoPodeHaverMaisDeQuatroRegistrosException(NaoPodeHaverMaisDeQuatroRegistrosException ex, WebRequest request) {
		return handleExceptionInternal(ex, new Erro(ex.getMessage()), new HttpHeaders(), HttpStatus.FORBIDDEN, request);
	}

	@ExceptionHandler({ NaoPodeRegistrarHorasEmFinalDeSemanaException.class })
	public ResponseEntity<Object> handleNaoPodeRegistrarHorasEmFinalDeSemanaException(NaoPodeRegistrarHorasEmFinalDeSemanaException ex, WebRequest request) {
		return handleExceptionInternal(ex, new Erro(ex.getMessage()), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}

	@ExceptionHandler({ NaoPossuiTempoDisponivelParaAlocacaoException.class })
	public ResponseEntity<Object> handleNaoPossuiTempoDisponivelParaAlocacaoException(NaoPossuiTempoDisponivelParaAlocacaoException ex, WebRequest request) {
		return handleExceptionInternal(ex, new Erro(ex.getMessage()), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}

	@ExceptionHandler({ NaoHaRegistrosDeTrabalhoNoMesException.class })
	public ResponseEntity<Object> handleNaoHaRegistrosDeTrabalhoNoMesException(NaoHaRegistrosDeTrabalhoNoMesException ex, WebRequest request) {
		return handleExceptionInternal(ex, new Erro(ex.getMessage()), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
	}
}
