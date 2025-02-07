package com.group11.accord.jpa.message;

import com.group11.accord.api.channel.Channel;
import com.group11.accord.api.message.Message;
import com.group11.accord.jpa.channel.ChannelJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<MessageJpa, Long> {

    List<MessageJpa> findByChannel(ChannelJpa channel);

}
