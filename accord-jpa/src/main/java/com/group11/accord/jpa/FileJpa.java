package com.group11.accord.jpa;

import jakarta.persistence.*;
import lombok.*;

enum FileType {
    PNG,
    JPEG
}

@Getter
@Setter
@Entity
@Builder
@Table(name = "file")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class FileJpa {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "file_id_seq")
    @SequenceGenerator(name = "file_id_seq", sequenceName = "file_id_seq", allocationSize = 1)
    @Setter(AccessLevel.NONE)
    private Long id;

    @NonNull
    @Column
    @Enumerated(EnumType.STRING)
    private FileType type;

    @NonNull
    @Column
    private Byte[] data;
}
