package org.acme.dto;

import org.acme.Localizacao;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

public class AdicionarRestauranteDTO {

    public String proprietario;
    public String CNPJ;
    public String nomeFantasia;
    public LocalizacaoDTO localizacao;

}
