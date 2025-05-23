package com.expoai.bucket.entity;

import com.expoai.bucket.enums.Visibility;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class UploadedImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner ;

    private Visibility visibility;

    // TODO Optimisation
    // Record not the URL but the UUID for the URL, Managing in the mapping the creation of the full URL in the returned DTO
    // This would also make it so, if the URL need to be change, that we wont need to reformat the whole DB
    @Column(nullable = true)
    private String publicUrl;
    // TODO delete this value and create it based on the UUID
    @Column(name = "public_thumbnail_url", nullable = true)
    private String publicThumbnailUrl;

    @Column(nullable = false)
    private String filename;

    @Column(nullable = false)
    private LocalDateTime createdAt ;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        UploadedImage that = (UploadedImage) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
