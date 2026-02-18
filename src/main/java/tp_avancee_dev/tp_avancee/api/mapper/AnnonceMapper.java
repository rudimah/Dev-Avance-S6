package tp_avancee_dev.tp_avancee.api.mapper;

import tp_avancee_dev.tp_avancee.api.dto.AnnonceResponseDto;
import tp_avancee_dev.tp_avancee.model.Annonce;

public class AnnonceMapper {

    public static AnnonceResponseDto toBasicDto(Annonce annonce) {
        if (annonce == null) {
            return null;
        }
        AnnonceResponseDto dto = new AnnonceResponseDto();
        dto.setId(annonce.getId());
        dto.setTitle(annonce.getTitle());
        dto.setDescription(annonce.getDescription());
        dto.setAdress(annonce.getAdress());
        dto.setMail(annonce.getMail());
        dto.setStatus(annonce.getStatus());

        dto.setDate(annonce.getCreatedAt());

        if (annonce.getAuthor() != null) {
            dto.setAuthorId(annonce.getAuthor().getId());
            dto.setAuthorName(annonce.getAuthor().getUsername());
        }

        if (annonce.getCategory() != null) {
            dto.setCategoryId(annonce.getCategory().getId());
            dto.setCategoryLabel(annonce.getCategory().getLabel());
        }

        return dto;
    }

    public static AnnonceResponseDto toDetailedDto(Annonce annonce) {
        return toBasicDto(annonce);
    }
}