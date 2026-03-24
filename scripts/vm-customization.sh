#!/bin/sh
if [ x$1 = x"precustomization" ]; then
    echo "Do Precustomization tasks"
elif [ x$1 = x"postcustomization" ]; then
    echo "Do Postcustomization tasks"
    mkdir -p /home/ansible/.ssh
    chmod 700 /home/ansible/.ssh
    echo "ssh-ed25519 AAAAC3NzaC1lZDI1NTE5AAAAIHfOOZ4THfEEsJzgJ49T8UMQVF3Ygn3HvaIAd6ME0teF Ansible" > /home/ansible/.ssh/authorized_keys
    chmod 600 /home/ansible/.ssh/authorized_keys
    chown -R ansible:ansible /home/ansible/.ssh
fi
